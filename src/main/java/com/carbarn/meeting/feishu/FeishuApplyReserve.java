package com.carbarn.meeting.feishu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonParser;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.contact.v3.model.*;
import com.lark.oapi.service.vc.v1.model.*;

import java.nio.charset.StandardCharsets;

public class FeishuApplyReserve {

    public static JSONObject createFeishuMeeting(String appId,
                                                 String appSecret,
                                                 String userIdType,
                                                 String ownerId,
                                                 String topic,
                                                 String end_time) {

        try {
            // 构建client
            Client client = Client.newBuilder(appId, appSecret).build();

            // 创建请求对象
            ApplyReserveReq req = ApplyReserveReq.newBuilder()
                    .userIdType(userIdType)
                    .applyReserveReqBody(ApplyReserveReqBody.newBuilder()
                            .endTime(end_time)
                            .ownerId(ownerId)
                            .meetingSettings(ReserveMeetingSetting.newBuilder()
                                    .topic(topic)
                                    .actionPermissions(new ReserveActionPermission[]{
                                            ReserveActionPermission.newBuilder()
                                                    .permission(1)
                                                    .permissionCheckers(new ReservePermissionChecker[]{
                                                            ReservePermissionChecker.newBuilder()
                                                                    .checkField(1)
                                                                    .checkMode(1)
                                                                    .checkList(new String[]{
                                                                            ownerId
                                                                    })
                                                                    .build()
                                                    })
                                                    .build()
                                    })
                                    .meetingInitialType(1)
                                    .callSetting(ReserveCallSetting.newBuilder()
                                            .callee(ReserveCallee.newBuilder()
                                                    .id(ownerId)
                                                    .userType(1)
//                                                .pstnSipInfo(PstnSipInfo.newBuilder()
//                                                        .nickname("dodo")
//                                                        .mainAddress("+86-02187654321")
//                                                        .build())
                                                    .build())
                                            .build())
                                    .autoRecord(false)
                                    .assignHostList(new ReserveAssignHost[]{
                                            ReserveAssignHost.newBuilder()
                                                    .userType(1)
                                                    .id(ownerId)
                                                    .build()
                                    })
//                                .password("971023")
                                    .build())
                            .build())
                    .build();

            // 发起请求
            ApplyReserveResp resp = client.vc().v1().reserve().apply(req);
            // 处理服务端错误
            if (!resp.success()) {
                return null;
            }
            String reserve = Jsons.DEFAULT.toJson(resp.getData().getReserve());
            return JSON.parseObject(reserve);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String inviteSellerJoinCompany(String appId,
                                                 String appSecret,
                                                 String phoneNum,
                                                 String name,
                                                 String departmentIdType,
                                                 String[] departmentIds) throws Exception {
        // 构建client
        Client client = Client.newBuilder(appId, appSecret).build();

        // 创建请求对象
        CreateUserReq req = CreateUserReq.newBuilder()
//                .userIdType(user_type)
                .departmentIdType(departmentIdType)
//                .clientToken("")
                .user(User.newBuilder()
//                        .userId(user_id)
                        .name(name)
                        .nickname(name)
                        .mobile(phoneNum)
                        .departmentIds(departmentIds)
                        .employeeType(1)
//                        .enName("")
//                        .nickname("")
//                        .mobileVisible()
//                        .gender()
//                        .avatarKey("")
//                        .leaderUserId("")
//                        .city("")
//                        .country("")
//                        .workStation("")
//                        .joinTime()
//                        .employeeNo("")
//                        .orders()
//                        .customAttrs()
//                        .enterpriseEmail("")
//                        .jobTitle("")
//                        .geo("cn")
//                        .jobLevelId("")
//                        .jobFamilyId("")
//                        .subscriptionIds()
//                        .dottedLineLeaderUserIds()
                        .build())
                .build();

        // 发起请求
        CreateUserResp resp = client.contact().v3().user().create(req);

        // 处理服务端错误
        if (!resp.success()) {
            System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                            resp.getCode(),
                            resp.getMsg(),
                            resp.getRequestId(),
                            Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8)))
                    )
            );
            return null;
        }
        // 业务数据处理
        System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
        return Jsons.DEFAULT.toJson(resp.getData());
    }


    public static String inviteBuyerJoinCompany(String appId,
                                                String appSecret,
                                                String phoneNum,
                                                String email,
                                                String name,
                                                String departmentIdType,
                                                String[] departmentIds) throws Exception {
        // 构建client
        Client client = Client.newBuilder(appId, appSecret).build();


        User.Builder userBuilder = null;
        if (phoneNum != null) {
            userBuilder = User.newBuilder()
                    .mobile(phoneNum);
        } else if (email != null) {
            userBuilder = User.newBuilder()
                    .email(email);
        } else {
            throw new Exception("phoneNum and email is null");
        }
        // 创建请求对象
        CreateUserReq req = CreateUserReq.newBuilder()
//                .userIdType(user_type)
                .departmentIdType(departmentIdType)
//                .clientToken("")
                .user(userBuilder
//                        .userId(user_id)
                        .name(name)
                        .nickname(name)
                        .departmentIds(departmentIds)
                        .employeeType(1)
//                        .enName("")
//                        .nickname("")
//                        .mobileVisible()
//                        .gender()
//                        .avatarKey("")
//                        .leaderUserId("")
//                        .city("")
//                        .country("")
//                        .workStation("")
//                        .joinTime()
//                        .employeeNo("")
//                        .orders()
//                        .customAttrs()
//                        .enterpriseEmail("")
//                        .jobTitle("")
//                        .geo("cn")
//                        .jobLevelId("")
//                        .jobFamilyId("")
//                        .subscriptionIds()
//                        .dottedLineLeaderUserIds()
                        .build())
                .build();

        // 发起请求
        CreateUserResp resp = client.contact().v3().user().create(req);

        // 处理服务端错误
        if (!resp.success()) {
            System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                            resp.getCode(),
                            resp.getMsg(),
                            resp.getRequestId(),
                            Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8)))
                    )
            );
            return null;
        }
        // 业务数据处理
        System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
        return Jsons.DEFAULT.toJson(resp.getData());
    }

    public static void deleteUserFromCompany(String appId,
                                             String appSecret,
                                             String user_id,
                                             String user_id_type) throws Exception {
        Client client = Client.newBuilder(appId, appSecret).build();

        // 创建请求对象
        DeleteUserReq req = DeleteUserReq.newBuilder()
                .userId(user_id)
                .userIdType(user_id_type)
                .deleteUserReqBody(DeleteUserReqBody.newBuilder()
                        .build())
                .build();

        // 发起请求
        DeleteUserResp resp = client.contact().v3().user().delete(req);
        // 处理服务端错误
        if (!resp.success()) {
            System.out.println(String.format("code:%s,msg:%s,reqId:%s, resp:%s",
                            resp.getCode(),
                            resp.getMsg(),
                            resp.getRequestId(),
                            Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8)))
                    )
            );
            return;
        }

        // 业务数据处理
    }

    public static void main(String[] args) throws Exception {
//        String app_link = "https://applink.feishu.cn/client/videochat/open?source=openplatform&action=join&idtype=reservationid&id=7497964974562115603&preview={?}&mic={?}&speaker={?}&camera={?}";
//        app_link = app_link.replace("preview={?}", "preview=1");
//        System.out.println(app_link);
//        JSONObject json = FeishuApplyReserve.createFeishuMeeting("cli_a89205333c3a500b",
//                "QylAyW3zPdoQZFvNm6Mp6fApcQcLynP8",
//                "open_id",
//                "ou_cca21789d04901886a710fd173efa0a6",
//                "车出海预约视频会议",
//                "1746456080");
//        System.out.println(json);


//        String result = FeishuApplyReserve.inviteSellerJoinCompany("cli_a89205333c3a500b",
//                "QylAyW3zPdoQZFvNm6Mp6fApcQcLynP8",
//                "19055144098",
//                "冰雪小米手机",
//                "open_department_id",
//                new String[]{"0"});

//        String lark_result = FeishuApplyReserve.inviteBuyerJoinCompany("cli_a8aaec9cd378102f",
//                "JDvh5tCRdooc1kVZO2ahUhe3yVBPjNIu",
//                "19055144098",
//                null,
//                "19055144098",
//                "open_department_id",
//                new String[]{"0"});

//        String lark_result = FeishuApplyReserve.inviteBuyerJoinCompany("cli_a8dc30df51b8d02f",
//                "usWys9FXsZZoOeJJOFB6kgXx3zy1vITm",
//                null,
//                "pop212029@163.com",
//                "pop212029@163.com",
//                "open_department_id",
//                new String[]{"0"});


//        System.out.println(result);
////
//        FeishuApplyReserve.deleteUserFromCompany("cli_a89205333c3a500b",
//                "QylAyW3zPdoQZFvNm6Mp6fApcQcLynP8",
//                "ou_3d03eae84ee9de99475f19a1ada28751",
//                "open_id");


//                FeishuApplyReserve.deleteUserFromCompany("cli_a8aaec9cd378102f",
//                "JDvh5tCRdooc1kVZO2ahUhe3yVBPjNIu",
//                "ou_58d67724799fa7321148f3d061488edf",
//                "open_id");

                        FeishuApplyReserve.deleteUserFromCompany("cli_a8dc30df51b8d02f",
                                "usWys9FXsZZoOeJJOFB6kgXx3zy1vITm",
                                "ou_31d4fd6abd5cf90826f517d8bd0a5ea2",
                                "open_id");


    }
}

