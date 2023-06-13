> 本文解决痛点。是否再不同项目需要不同的OSS二头疼。
>
> - A项目用七牛云，B项目使用阿里云。
> - 不想用七牛云了，还是改用华为云吧。
> - 同个项目使用不同的 bucketName
>
> 遇到这种种情况，本文提供一个依赖搞定多云OSS 适配问题

# 什么是OSS?

数据以对象（Object）的形式存储在OSS的存储空间（Bucket
）中。如果要使用OSS存储数据，您需要先创建Bucket，并指定Bucket的地域、访问权限、存储类型等属性。创建Bucket后，您可以将数据以Object的形式上传到Bucket，并指定Object的文件名（Key）作为其唯一标识。

OSS以HTTP RESTful API的形式对外提供服务，访问不同地域需要不同的访问域名（Endpoint）。当您请求访问OSS时，OSS通过使用访问密钥（AccessKey
ID和AccessKey Secret）对称加密的方法来验证某个请求的发送者身份。

## OSS在项目中的使用

OSS对象存储在目前大部分项目中必不可少的存在，如下图所示。

![image-20230417163846184](http://md7.admin4j.com/blog/SpringCloud/image-20230417163846184.png)

1. 一般项目使用OSS对象存储服务，主要是对图片、文件、音频等对象集中式管理权限控制，管理数据生命周期等等，提供上传，下载，预览，删除等功能。
2. 通过OSS部署前端项目。
3. 配合CDN使用
4. 减少对业务服务器的流量

# 什么是AmazonS3

> - [https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/Welcome.html](https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/Welcome.html)

Amazon Simple Storage Service（Amazon S3，Amazon简便存储服务）是 AWS 最早推出的云服务之一，经过多年的发展，S3
协议在对象存储行业事实上已经成为标准。

1. 提供了统一的接口 REST/SOAP 来统一访问任何数据
2. 对 S3 来说，存在里面的数据就是对象名（键），和数据（值）
3. 不限量，单个文件最高可达 5TB，可动态扩容。
4. 高速。每个 bucket 下每秒可达 3500 PUT/COPY/POST/DELETE 或 5500 GET/HEAD 请求。
5. 具备版本，权限控制能力
6. 具备数据生命周期管理能力

作为一个对象存储服务，S3 功能真的很完备，行业的标杆，目前市面上大部分OSS对象存储服务都支持AmazonS3，*
*本文主要讲解的就是基于AmazonS3实现我们自己的** **Spring Boot Starter。**

## 阿里云OSS兼容S3

[https://help.aliyun.com/document_detail/451966.html?spm=a2c4g.389025.0.0.170a27a2KuNpvV](https://help.aliyun.com/document_detail/451966.html?spm=a2c4g.389025.0.0.170a27a2KuNpvV)

![image-20230417163733831](http://md7.admin4j.com/blog/SpringCloud/image-20230417163733831.png)

## 七牛云对象存储兼容S3

[https://developer.qiniu.com/kodo/4088/s3-access-domainname](https://developer.qiniu.com/kodo/4088/s3-access-domainname)

![image-20230417164035296](http://md7.admin4j.com/blog/SpringCloud/image-20230417164035296.png)

## 腾讯云COS兼容S3

[https://cloud.tencent.com/document/product/436/41284](https://cloud.tencent.com/document/product/436/41284)

![image-20230417164202501](http://md7.admin4j.com/blog/SpringCloud/image-20230417164202501.png)

## Minio兼容S3

[http://minio.org.cn/product/s3-compatibility.html](http://minio.org.cn/product/s3-compatibility.html)

![image-20230417164311776](http://md7.admin4j.com/blog/SpringCloud/image-20230417164311776.png)

# 我们为什么要基于**AmazonS3实现** **Spring Boot Starter**

原因：市面上OSS对象存储服务基本都支持AmazonS3，我们封装我们的自己的starter那么就必须考虑适配，迁移，可扩展。比喻说我们今天使用的是阿里云OSS对接阿里云OSS的SDK，后天我们使用的是腾讯COS对接是腾讯云COS，我们何不直接对接AmazonS3实现呢，这样后续不需要调整代码，只需要去各个云服务商配置就好了

## 使用

引入

```
<dependency>
    <groupId>com.admin4j.framework</groupId>
    <artifactId>oss-spring-boot-starter</artifactId>
    <version>0.1.2</version>
</dependency>
```

`oss-spring-boot-starter`
最新版查询 [https://central.sonatype.com/artifact/com.admin4j.framework/oss-spring-boot-starter/](https://central.sonatype.com/artifact/com.admin4j.framework/oss-spring-boot-starter/)

配置文件

```
admin4j:
  oss:
    endpoint: "http://192.168.1.13:9901"
    region: cn-east-1
    access-key: 4NdsQG6g6hzJfwko
    secret-key: zeSSPz3WC3Wn4zBZsYtxK9YXhZ7Hjnnv
    bucket-name: oss-template
    enable: true
```

### OssTemplate 使用直接操作 OSS

```
@SpringBootTest
public class OssTest {

    @Autowired
    OssTemplate ossTemplate;

    @Test
    /**
     * 创建 Bucket
     */
    public void testCreateBucket() {

        ossTemplate.createBucket("oss-template");
    }

    @Test
    /**
     * 获取所有Bucket
     */
    public void testGetAllBuckets() {

        List<Bucket> allBuckets = ossTemplate.getAllBuckets();
        System.out.println("allBuckets = " + allBuckets);
    }

    @Test
    /**
     * 上传文件
     */
    public void testPut() throws IOException {

        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\andanyang\\Downloads\\012408-167976504839ee.jpg");
        PutObjectResult putObjectResult = ossTemplate.putObject("oss-template", "test/Test.jpg", fileInputStream);
        System.out.println("putObjectResult = " + putObjectResult);
    }

    @Test
    /**
     * 获取文件
     */
    public void testGet() throws IOException {

        String objectURL = ossTemplate.getObjectURL("oss-template", "test/Test.jpg", 1, TimeUnit.DAYS);
        System.out.println("objectURL = " + objectURL);
    }


}
```

### UploadFileService web 上传文件

```
@RestController
@RequestMapping("oss")
public class OssController {

    @Autowired
    UploadFileService uploadFileService;

    @PutMapping
    public R upload(MultipartFile file) throws IOException {


        UploadFileVO image = uploadFileService.upload("image", file);

        System.out.println("image = " + image);

        String previewUrl = uploadFileService.getPreviewUrl(image.getKey());

        System.out.println("previewUrl = " + previewUrl);

        return R.ok(image);
    }
}
```

## 自动以上传文件

继承`SimpleOSSUploadFileService`

- 重写 `generateFilePath`() 方法 ，自定义生成文件名
- 重写 `beforeUpload`  上传前钩子 true 可以上传 false 不用上传（比如根据md5/path 检查文件已存在）
- 重写 `afterUpload`  上传完成钩子，可以保存上传记录等
- 注入多个继承于`SimpleOSSUploadFileService` bean,可以实现同个项目，上传到不同OSS或者不同Bucket的目的

# 项目地址

[https://github.com/admin4j/admin4j-framework/tree/master/oss-spring-boot-starter](https://github.com/admin4j/admin4j-framework/tree/master/oss-spring-boot-starter)
