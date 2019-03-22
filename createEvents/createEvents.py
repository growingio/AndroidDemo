# -*- coding=utf-8 -*-
import pandas as pd
import hashlib
import hmac
import time
import requests as req
import json
import configparser
import os

current_path = os.path.dirname(os.path.realpath(__file__))
config_path = os.path.join(current_path, "config.ini")

conf = configparser.ConfigParser()
conf.read(config_path, encoding="utf-8")  
sections = conf.sections()

project_id = conf.get('GrowingIOProjectInfo', "project_id")
project_key = conf.get('GrowingIOProjectInfo', "project_key")
private_key = conf.get('GrowingIOProjectInfo', "private_key")
project_uid = conf.get('GrowingIOProjectInfo', "project_uid")

def auth_token(secret, project, ai, tm):
    """计算 auth """
    message = ("POST\n/auth/token\nproject=" + project + "&ai=" + ai + "&tm=" + tm).encode('utf-8')
    signature = hmac.new(bytes(secret.encode('utf-8')), bytes(message), digestmod=hashlib.sha256).hexdigest()
    return signature


def get_token():
    tm_stamp = str(round(time.time() * 1000))
    # 私钥、project uid , ai , tm
    auth = auth_token(private_key, project_uid, project_id, tm_stamp)
    # header 中是 公钥
    header = {'X-Client-Id': project_key}
    url_token = 'https://www.growingio.com/auth/token?ai={ai}&project={project}&tm={tm}&auth={auth}'.format(
        ai=project_id,
        project=project_uid,
        tm=tm_stamp,
        auth=auth
    )
    # 
    token = req.post(url_token, headers=header)
    token = json.loads(token.text)
    return token['code']


headerEvents = {'X-Client-Id': project_key, 'Authorization': get_token()}

def events():
    """创建事件级变量"""
    cstm_url = 'https://www.growingio.com/v1/api/projects/{}/vars/events'.format(project_uid)
    with open('events.csv' if conf.get('UploadEventsFileName','cstmFileName') == '' else 'events.csv') as file:
        track_file = pd.read_csv(file, encoding="utf_8", index_col=0)
        for index, row in track_file.iterrows():
            cstm_data = {
                'type': row['type'],
                'description': row['description'],
                'name': row['name'],
                'key': row['key']
            }
            print('上传事件级变量: ', cstm_data)
            r = req.post(cstm_url, headers=headerEvents, json=cstm_data)
            if r.status_code == 200:
                print(r.text)
            else:
                print(r.status_code, r.text)


def get_cstm_events_variable():
    """获取事件级变量，为了拿到服务端分配的 id """
    get_cstm_events_url = 'https://www.growingio.com/v1/api/projects/{}/vars/events'.format(project_uid)
    requests = req.get(get_cstm_events_url, headers=headerEvents)
    if requests.status_code == 200:
        print('---------获取事件级变量-----------')
        events = pd.read_json(requests.text)
        events.to_csv("事件.csv")
        print('---------生成表格 CSTM_Events.csv 成功 -----------')
    else:
        print('---------获取事件级变量失败-----------\n')
        print(requests.status_code,"-->",requests.text)

def get_vars_events_variable():
    """获取事件列表变量"""
    get_cstm_events_url = 'https://www.growingio.com/v1/api/projects/{}/dim/events'.format(project_uid)
    requests = req.get(get_cstm_events_url, headers=headerEvents)
    if requests.status_code == 200:
        print('---------获取事件列表-----------')
        events = pd.read_json(requests.text)
        events.to_csv("事件级变量.csv")
        print('---------生成表格 getEvents.csv 成功 -----------')
    else:
        print('---------获取事件列表失败-----------\n')
        print(requests.status_code,"-->",requests.text)

def get_page_events_variable():
    """获取页面级变量"""
    get_cstm_events_url = 'https://www.growingio.com/v1/api/projects/{}/vars/pages'.format(project_uid)
    requests = req.get(get_cstm_events_url, headers=headerEvents)
    if requests.status_code == 200:
        print('---------获取页面级变量-----------')
        events = pd.read_json(requests.text)
        events.to_csv("页面级变量.csv")
        print('---------生成表格 getEvents.csv 成功 -----------')
    else:
        print('---------error 获取页面级变量失败-----------\n')
        print(requests.status_code,"-->",requests.text)

def get_ppl_events_variable():
    """获取登录用户变量"""
    get_cstm_events_url = 'https://www.growingio.com/v1/api/projects/{}/vars/peoples'.format(project_uid)
    requests = req.get(get_cstm_events_url, headers=headerEvents)
    if requests.status_code == 200:
        print('---------获取登录用户变量-----------')
        events = pd.read_json(requests.text)
        events.to_csv("登录用户变量.csv")
        print('---------生成表格 登录用户变量.csv 成功 -----------')
    else:
        print('---------error 获取登录用户变量失败-----------\n')
        print(requests.status_code,"-->",requests.text)




def cstm_events():
    """创建打点事件"""
    events_url = 'https://www.growingio.com/v1/api/projects/{}/dim/events'.format(project_uid)
    dict_events = {}
    with open('getEvents.csv') as file:
        events = pd.read_csv(file, encoding="utf_8", index_col=0)
        for index, row in events.iterrows():
            dict_events[row['name']] = {"key": row['key'], "id": row['id'], "type": row['type'], "name": row['name']}

    with open('cstmEvents.csv' if conf.get('UploadEventsFileName','cstm_eventsFileName') == '' else 'cstm_events.csv') as file:
        track_file = pd.read_csv(file, encoding="utf_8", index_col=0)
        attrs = []
        cstm_data = []
        for index, row in track_file.iterrows():
            var = row['事件级变量'].split("、")
            for key in var:
                key = key.strip()
                if key != '':
                    attrs.append(dict_events[key])
            cstm_data.append({
                'attrs': attrs,
                'type': 'counter' if row['类型'] == '计数器' else 'number',
                'description': row['描述'],
                'name': index,
                'key': row['标识符'].strip()
            })
        print(json.dumps(cstm_data))
        r = req.post(events_url, headers=headerEvents, json=cstm_data)
        if r.status_code != 200:
            print("自定义事件设置失败：", r.status_code, "服务器返回：", r.text)
        else:
            print(r.text)


def pvar_events():
    """创建页面级变量"""
    pvar_url = 'https://www.growingio.com/v1/api/projects/{}/vars/pages'.format(project_uid)
    with open('pvarEvents.csv' if conf.get('UploadEventsFileName','pvarFileName') == '' else 'pvarEvents.csv') as file:
        track_file = pd.read_csv(file, encoding="utf_8", index_col=0)
        for index, row in track_file.iterrows():
            pvar_data = {
                'description': row['description'],
                'name': row['name'],
                'key': row['key']
            }
            r = req.post(pvar_url, headers=headerEvents, json=pvar_data)
            if r.status_code != 200:
                print("自定义事件设置失败：", r.status_code, "服务器返回：", r.text)
            else:
                print(r.text)


def pplEvents():
    """创建登录用户变量"""
    ppl_url = 'https://www.growingio.com/v1/api/projects/{}/vars/peoples'.format(project_uid)
    with open('pplEvents.csv' if conf.get('UploadEventsFileName','pplFileName') == '' else 'pplEvents.csv') as file:
        track_file = pd.read_csv(file, encoding="utf_8", index_col=0)
        for index, row in track_file.iterrows():
            ppl_data = [
                {
                    'description': row['description'],
                    'name': index,
                    'key': row['key'],
                    'attribution': row['attribution']
                }
            ]
            print(ppl_data)
            r = req.post(ppl_url, headers=headerEvents, json=ppl_data)
            if r.status_code != 200:
                print("创建登录用户变量失败：", r.status_code, "服务器返回：", r.text)
            else:
                print(r.text)




if(conf.getboolean('Actions', "uploadEvents")):
    events()
    get_cstm_events_variable()
    cstm_events()
    pvar_events()
    pplEvents()

if(conf.get('Actions', "getEvents")):
    get_vars_events_variable()
    get_cstm_events_variable()
    get_page_events_variable()
    get_ppl_events_variable()
