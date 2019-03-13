#-*- coding=utf-8 -*-
import emoji
import pandas as pd
import hashlib
import hmac
import time
import requests as req
import json

project_id = '97fd6815651f25fb'
publicKey = '06b3f5eb364f410eb7435a6940c4f431'
privateKey = '3bde47c09c534a0db10a4b2402d3cd46'
project_uid= 'z98jGyZP'

# 创建事件级变量，将事件级变量 csv 文件名填入，默认 events.csv
cstmFileName = ''
# 创建事件，将您的自定义事件文件名输入，默认 cstmEvents.csv
cstmEventsFileName = ''
# 创建页面级变量
pvarFileName = ''
# 创建登录用户变量
pplFileName = ''

def authToken(secret, project, ai, tm):
    """计算 auth """
    message = ("POST\n/auth/token\nproject=" + project + "&ai=" + ai + "&tm=" + tm).encode('utf-8')
    signature = hmac.new(bytes(secret.encode('utf-8')), bytes(message), digestmod=hashlib.sha256).hexdigest()
    return signature

def getToken():
    tmStamp = str(round(time.time() * 1000))
    print ('使用时间戳 tm -> '+tmStamp)

    # 私钥、project uid , ai , tm
    authStr = authToken(privateKey, project_uid, project_id,tmStamp)
    print('authToken 计算 -> '+authStr)

    # header 中是 公钥
    header = {'X-Client-Id':publicKey}
    urlToken = 'https://www.growingio.com/auth/token?ai={ai}&project={project}&tm={tm}&auth={auth}'.format(
        ai=project_id,
        project=project_uid,
        tm=tmStamp,
        auth=authStr
        )
    # 
    token = req.post(urlToken,headers = header)
    print("token 请求返回 -> "+token.text)
    tokenReturn = json.loads(token.text)
    print('返回 token 啦 -> '+ tokenReturn['code'])
    return tokenReturn['code']

headerEvents = {'X-Client-Id':publicKey,'Authorization':getToken()}
print(headerEvents)

def events():
    """创建事件级变量"""
    cstmUrl = 'https://www.growingio.com/v1/api/projects/{}/vars/events'.format(project_uid)
    with open('events.csv' if cstmFileName == '' else cstmFileName) as file:
        trackFile = pd.read_csv(file,encoding = "utf_8",index_col=0)
        for index,row in trackFile.iterrows():
            cstmData={
                'type':row['type'],
                'description':row['description'],
                'name':row['name'],
                'key':row['key']
                }
            print('上传事件级变量: ', cstmData)
            r = req.post(cstmUrl,headers=headerEvents,json=cstmData)
            if (r.status_code ==200):
                print(emoji.emojize(':thumbs_up:'),r.text)
            else:
                print(emoji.emojize(':broken_heart:'),r.status_code,r.text)
           
            

def getCstmEventsVariable():
    """获取事件级变量，为了拿到服务端分配的 id """
    getCstmEventsUrl= 'https://www.growingio.com/v1/api/projects/{}/vars/events'.format(project_uid)
    requests = req.get(getCstmEventsUrl,headers = headerEvents)
    if(requests.status_code == 200):
        print('---------获取事件级变量-----------')
        events = pd.read_json(requests.text)
        events.to_csv("getEvents.csv")
        print('---------生成表格 getEvents.csv 成功 -----------')


def cstmEvents():
    """创建打点事件"""
    eventsUrl = 'https://www.growingio.com/v1/api/projects/{}/dim/events'.format(project_uid)
    dictEvents = {}
    with open('getEvents.csv') as file:
        events = pd.read_csv(file,encoding = "utf_8",index_col=0)
        for index,row in events.iterrows():
            dictEvents[row['name']] = {"key": row['key'],"id": row['id'], "type": row['type'],"name": row['name']}

    with open('cstmEvents.csv' if cstmEventsFileName == '' else cstmEventsFileName) as file:
        trackFile = pd.read_csv(file,encoding = "utf_8",index_col=0)
        attrs = []
        cstmData=[]
        for index,row in trackFile.iterrows():
            var = row['事件级变量'].split("、")
            for key in var:
                key = key.strip()
                if key != '':
                    attrs.append(dictEvents[key])
            cstmData.append({
                'attrs':attrs,
                'type': 'counter' if row['类型'] == '计数器' else 'number',
                'description':row['描述'],
                'name':index,
                'key':row['标识符'].strip()
            })
        print(json.dumps(cstmData))
        r = req.post(eventsUrl,headers=headerEvents,json=cstmData)
        if r.status_code != 200 :
            print("自定义事件设置失败：",r.status_code,"服务器返回：",r.text)
        else:
            print(emoji.emojize(':thumbs_up:'),r.text)

def pvarEvents():
    """创建页面级变量"""
    pvarUrl = 'https://www.growingio.com/v1/api/projects/{}/vars/pages'.format(project_uid)
    with open('pvarEvents.csv' if pvarFileName == '' else pvarFileName) as file:
        trackFile = pd.read_csv(file,encoding = "utf_8",index_col=0)
        for index,row in trackFile.iterrows():
            pvarData = {
                'description':row['description'],
                'name':row['name'],
                'key':row['key']
            }
            r = req.post(pvarUrl,headers=headerEvents,json=pvarData)
            if r.status_code != 200 :
                print("自定义事件设置失败：",r.status_code,"服务器返回：",r.text)
            else:
                print(emoji.emojize(':thumbs_up:'),r.text)
            

def pplEvents():
    """创建登录用户变量"""
    pplUrl = 'https://www.growingio.com/v1/api/projects/{}/vars/peoples'.format(project_uid)
    with open('pplEvents.csv' if pplFileName == '' else pplFileName) as file:
        trackFile = pd.read_csv(file,encoding = "utf_8",index_col=0)
        for index,row in trackFile.iterrows():
            pplData = [
                {
                'description':row['description'],
                'name':index,
                'key':row['key'],
                'attribution':row['attribution']
                }
            ]
            print(pplData)
            r = req.post(pplUrl,headers=headerEvents,json=pplData)
            if r.status_code != 200 :
                print("创建登录用户变量失败：",r.status_code,"服务器返回：",r.text)
            else:
                print(emoji.emojize(':thumbs_up:'),r.text)


  
events()
getCstmEventsVariable()
cstmEvents()
pvarEvents()
pplEvents()

