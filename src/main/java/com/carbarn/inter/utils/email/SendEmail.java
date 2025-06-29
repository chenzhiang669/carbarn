package com.carbarn.inter.utils.email;


import com.aliyun.dm20151123.models.SingleSendMailResponse;
import com.aliyun.tea.TeaException;

public class SendEmail {

    public static com.aliyun.dm20151123.Client createClient(String accessKeyId,
                                                            String accessKeySecret) throws Exception {
        // 工程代码建议使用更安全的无AK方式，凭据配置方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.credentials.Client credential = new com.aliyun.credentials.Client();
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setCredential(credential)
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dm
        config.endpoint = "dm.aliyuncs.com";
        return new com.aliyun.dm20151123.Client(config);
    }

    public static boolean sendEmail(String accountName,
                                 int addressType,
                                 boolean replyToAddress,
                                 String toAddress,
                                 String subject,
                                 String body,
                                 String fromAlias,
                                 String accessKeyId,
                                 String accessKeySecret) {
        try {
            com.aliyun.dm20151123.Client client = createClient(accessKeyId, accessKeySecret);
            com.aliyun.dm20151123.models.SingleSendMailRequest singleSendMailRequest = new com.aliyun.dm20151123.models.SingleSendMailRequest()
                    .setAccountName(accountName)
                    .setAddressType(addressType)
                    .setReplyToAddress(replyToAddress)
                    .setToAddress(toAddress)
                    .setSubject(subject)
                    .setHtmlBody(body)
                    .setFromAlias(fromAlias);

            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

            // 复制代码运行请自行打印 API 的返回值
            SingleSendMailResponse singleSendMailResponse = client.singleSendMailWithOptions(singleSendMailRequest, runtime);
            if(singleSendMailResponse.getStatusCode() == 200){
                return true;
            }
        } catch (TeaException error) {
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        //        com.aliyun.dm20151123.models.SingleSendMailRequest singleSendMailRequest = new com.aliyun.dm20151123.models.SingleSendMailRequest()
//                .setAccountName("carvata@mail.chechuhai.top")
//                .setAddressType(1)
//                .setReplyToAddress(true)
//                .setToAddress("1461251592@qq.com")
//                .setSubject("111111 is your Carvata verification code")
//                .setHtmlBody("Here is your Carvata verification code. <br/> Please enter it soon before it expires in 10 minutes: <br/>111111 <br/> Carvata Team")
//                .setFromAlias("Carvata Team");
        sendEmail("carvata@mail.chechuhai.top",
                1,
                true,
                "1461251592@qq.com",
                "222222 is your Carvata verification code",
                "Here is your Carvata verification code. <br/> Please enter it soon before it expires in 10 minutes: <br/>222222 <br/> Carvata Team",
                "Carvata Team",
                "",
                "");
    }
}
