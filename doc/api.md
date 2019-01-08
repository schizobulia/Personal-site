目录

1\. 接口查询

---

**1\. key的生成**
###### 接口功能
> 生成key值

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 接口示例
> 地址：[http://58.87.85.53:8002/personal-0.1/createkey](http://58.87.85.53:8002/personal-0.1/createkey)
``` javascript
    1546836795828-628759
```
---

**2\. 使用sql将文件过滤**
###### 接口功能
> 文件过滤

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### HTTP请求参数
> { 'sql': 'select * from qs', "key": "1546925760639-654869", "files": "['a.json', 'b.json']" }

###### 接口示例
> 地址：[http://58.87.85.53:8002/personal-0.1/parsing](http://58.87.85.53:8002/personal-0.1/parsing)
``` javascript
{
    "statue": 1,
    "files": ["part-00000-5f89bc44-6df5-420d-9652-05d0e79d59b0-c000.json"],
    "static": "/parsingfile/out/1546925724258-612195-select * from qs/"
}
```
