#-*- coding=utf-8 -*-
import pandas as pd
import hashlib
import hmac
import time
import requests as req
import json

def authToken(secret, project, ai, tm):
    message = ("POST\n/auth/token\nproject=" + project + "&ai=" + ai + "&tm=" + tm).encode('utf-8')
    signature = hmac.new(bytes(secret.encode('utf-8')), bytes(message), digestmod=hashlib.sha256).hexdigest()
    return signature

tmStamp = str(round(time.time() * 1000))
print ('tm -> '+tmStamp)

# 私钥、project uid , ai , tm
authStr = authToken('3bde47c09c534a0db10a4b2402d3cd46', 'z98jGyZP', '97fd6815651f25fb',tmStamp)

print('authToken 计算 -> '+authStr)

# header 中是 公钥
header = {'X-Client-Id':'06b3f5eb364f410eb7435a6940c4f431'}
urlToken = 'https://www.growingio.com/auth/token?ai={ai}&project={project}&tm={tm}&auth={auth}'.format(
    ai='97fd6815651f25fb',
    project='z98jGyZP',
    tm=tmStamp,
    auth=authStr
    )
# 
token = req.post(urlToken,headers = header)

print("token 请求返回 -> "+token.text)
tokenReturn = json.loads(token.text)
print('返回 token 啦 -> '+ tokenReturn['code'])

# 公钥、token
headerEvents = {'X-Client-Id':'06b3f5eb364f410eb7435a6940c4f431','Authorization':tokenReturn['code']}
print("header -> "+str(headerEvents))
def events():
    """创建事件级变量"""
    cstmUrl = 'https://www.growingio.com/v1/api/projects/{project_id}/vars/events'.format(project_id = '97fd6815651f25fb')
    with open('events.csv') as file:
        trackFile = pd.read_csv(file,encoding = "utf_8",index_col=0)
        for index,row in trackFile.iterrows():
            if index != 'id1':
                break
            cstmData={
                'type':row['type'],
                'description':row['description'],
                'name':row['name'],
                'key':row['key']
                }
            print('data: ', cstmData)
            r = req.post(cstmUrl,headers=headerEvents,json=cstmData)
            print(r.text)
            print(r.status_code)

def cstmEvents():
    """创建自定义事件"""
    events = 'https://www.growingio.com/v1/api/projects/{project_id}/dim/events'.format(project_id = '97fd6815651f25fb')


events()
cstmEvents()

    
