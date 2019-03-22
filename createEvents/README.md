# GrowingIO API Script

## 1. 功能介绍
批量导出和导入 GrowingIO 的自定义事件和变量
  
## 2. 使用步骤  
* ### 环境准备  
    python3 下载和安装，请自行百度， 友情贴出链接   
    windows : https://www.jianshu.com/p/7a0b52075f70  
    mac : https://pythonguidecn.readthedocs.io/zh/latest/starting/install3/osx.html 

* ### 下载脚本  
    会使用 Git 的同学， 可以直接 clone 本项目，
    不会使用 Git 的同学， 点击下载。

    下载完成脚本后，执行以下操作安装依赖库：
    ```
    # 如果目录文件名是 GrowingIOAutoTrack-master
    cd GrowingIOAutoTrack-master 
    pip install pandas
    pip install requests
    ```

* ### 配置文件填写    
    编辑文件 config.ini，填写文件中对应字段。  

    字段获取方式：  
|  字段名称   	|   获取方式	|
|---	|---	|
|   project_id	|官网登陆后->右上角设置小图标-项目概览->左侧基本信息中的“项目ID”|
|   publicKey	|同上项目概览页面，复制“项目公钥”|
|   privateKey	|同上项目概览页面，复制“项目私钥”|
|   project_uid	|点击GrowingIO Logo，进入“首页”，复制url中对应字段-> https://www.growingio.com/projects/复制这里的字符串/homepage/overview	|
* ### 执行脚本

    ```
    python3 ./createEvents.py
    ```

* ### 上传自定义事件表格命名对应事件
    