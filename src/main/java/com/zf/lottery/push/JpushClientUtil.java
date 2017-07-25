package com.zf.lottery.push;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JpushClientUtil {
	private final static String appKey = "df4351eebe0b1d174825853b";
	private final static String masterSecret = "99b9d5b3de7c8adb64367cef";
	private static JPushClient jPushClient = new JPushClient(masterSecret, appKey);

	/**
	 * ���͸��豸��ʶ�������û�
	 * 
	 * @param registrationId
	 *            �豸��ʶ
	 * @param notification_title
	 *            ֪ͨ���ݱ���
	 * @param msg_title
	 *            ��Ϣ���ݱ���
	 * @param msg_content
	 *            ��Ϣ����
	 * @param extrasparam
	 *            ��չ�ֶ�
	 * @return 0����ʧ�ܣ�1���ͳɹ�
	 */
	public static int sendToRegistrationId(String registrationId, String notification_title, String msg_title,
			String msg_content, String extrasparam) {
		int result = 0;
		try {
			PushPayload pushPayload = JpushClientUtil.buildPushObject_all_registrationId_alertWithTitle(registrationId,
					notification_title, msg_title, msg_content, extrasparam);
			System.out.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.out.println(pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (APIConnectionException e) {
			e.printStackTrace();

		} catch (APIRequestException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * ���͸����а�׿�û�
	 * 
	 * @param notification_title
	 *            ֪ͨ���ݱ���
	 * @param msg_title
	 *            ��Ϣ���ݱ���
	 * @param msg_content
	 *            ��Ϣ����
	 * @param extrasparam
	 *            ��չ�ֶ�
	 * @return 0����ʧ�ܣ�1���ͳɹ�
	 */
	public static int sendToAllAndroid(String notification_title, String msg_title, String msg_content,
			String extrasparam) {
		int result = 0;
		try {
			PushPayload pushPayload = JpushClientUtil.buildPushObject_android_all_alertWithTitle(notification_title,
					msg_title, msg_content);
			System.out.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.out.println(pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	/**
	 * ���͸�����IOS�û�
	 * 
	 * @param notification_title
	 *            ֪ͨ���ݱ���
	 * @param msg_title
	 *            ��Ϣ���ݱ���
	 * @param msg_content
	 *            ��Ϣ����
	 * @param extrasparam
	 *            ��չ�ֶ�
	 * @return 0����ʧ�ܣ�1���ͳɹ�
	 */
	public static int sendToAllIos(String notification_title, String msg_title, String msg_content,
			String extrasparam) {
		int result = 0;
		try {
			PushPayload pushPayload = JpushClientUtil.buildPushObject_ios_all_alertWithTitle(notification_title,
					msg_title, msg_content, extrasparam);
			System.out.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.out.println(pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	/**
	 * ���͸������û�
	 * 
	 * @param notification_title
	 *            ֪ͨ���ݱ���
	 * @param msg_title
	 *            ��Ϣ���ݱ���
	 * @param msg_content
	 *            ��Ϣ����
	 * @param extrasparam
	 *            ��չ�ֶ�
	 * @return 0����ʧ�ܣ�1���ͳɹ�
	 */
	public static int sendToAll(String notification_title, String msg_title, String msg_content, String extrasparam) {
		int result = 0;
		try {
			PushPayload pushPayload = JpushClientUtil.buildPushObject_android_and_ios(notification_title, msg_title,
					msg_content, extrasparam);
			System.out.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.out.println(pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public static PushPayload buildPushObject_android_and_ios(String notification_title, String msg_title,
			String msg_content, String extrasparam) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.all())
				.setNotification(Notification.newBuilder().setAlert(notification_title)
						.addPlatformNotification(AndroidNotification.newBuilder().setAlert(notification_title)
								.setTitle(notification_title)
								// ���ֶ�Ϊ͸���ֶΣ�������ʾ��֪ͨ�����û�����ͨ�����ֶ�����һЩ�����������ض���key��Ҫָ����ת��ҳ�棨value��
								.addExtra("androidNotification extras key", extrasparam).build())
						.addPlatformNotification(IosNotification.newBuilder()
								// ��һ��IosAlert����ָ��apns title��title��subtitle��
								.setAlert(notification_title)
								// ֱ�Ӵ�alert
								// ������ָ�������͵�badge�Զ���1
								.incrBadge(1)
								// ���ֶε�ֵdefault��ʾϵͳĬ����������sound.caf��ʾ����������Ŀ��������sound.caf���������ѣ�
								// ���ϵͳû�д���Ƶ����ϵͳĬ���������ѣ����ֶ���������ַ�����iOS9�����ϵ�ϵͳ�����������ѣ����µ�ϵͳ��Ĭ������
								.setSound("sound.caf")
								// ���ֶ�Ϊ͸���ֶΣ�������ʾ��֪ͨ�����û�����ͨ�����ֶ�����һЩ�����������ض���key��Ҫָ����ת��ҳ�棨value��
								.addExtra("iosNotification extras key", extrasparam)
								// ����˵����������һ��background���ͣ����˽�background����http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
								// .setContentAvailable(true)

								.build())
						.build())
				// Platformָ������Щƽ̨�ͻ���ָ��ƽ̨�з��������������豸�������͡� jpush���Զ�����Ϣ��
				// sdkĬ�ϲ����κδ���������֪ͨ��ʾ�����鿴�ĵ�http://docs.jpush.io/guideline/faq/��
				// [֪ͨ���Զ�����Ϣ��ʲô����]�˽�֪ͨ���Զ�����Ϣ������
				.setMessage(Message.newBuilder().setMsgContent(msg_content).setTitle(msg_title)
						.addExtra("message extras key", extrasparam).build())

				.setOptions(Options.newBuilder()
						// ���ֶε�ֵ������ָ��������Ҫ���͵�apns������false��ʾ������true��ʾ��������android���Զ�����Ϣ������
						.setApnsProduction(false)
						// ���ֶ��Ǹ��������Լ������ͱ�ţ����������߷ֱ����ͼ�¼
						.setSendno(1)
						// ���ֶε�ֵ������ָ�������͵����߱���ʱ��������������ֶ���Ĭ�ϱ���һ�죬���ָ������ʮ�죬��λΪ��
						.setTimeToLive(86400).build())
				.build();
	}

	private static PushPayload buildPushObject_all_registrationId_alertWithTitle(String registrationId,
			String notification_title, String msg_title, String msg_content, String extrasparam) {

		System.out.println("----------buildPushObject_all_all_alert");
		// ����һ��IosAlert���󣬿�ָ��APNs��alert��title���ֶ�
		// IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody("title",
		// "alert body").build();

		return PushPayload.newBuilder()
				// ָ��Ҫ���͵�ƽ̨��all����ǰӦ�������˵�����ƽ̨��Ҳ���Դ�android�Ⱦ���ƽ̨
				.setPlatform(Platform.all())
				// ָ�����͵Ľ��ն���all���������ˣ�Ҳ����ָ���Ѿ����óɹ���tag��alias���ӦӦ�ÿͻ��˵��ýӿڻ�ȡ����registration
				// id
				.setAudience(Audience.registrationId(registrationId))
				// jpush��֪ͨ��android����jpushֱ���·���iOS����apns�������·���Winphone����mpns�·�
				.setNotification(Notification.newBuilder()
						// ָ����ǰ���͵�android֪ͨ
						.addPlatformNotification(AndroidNotification.newBuilder()

								.setAlert(notification_title).setTitle(notification_title)
								// ���ֶ�Ϊ͸���ֶΣ�������ʾ��֪ͨ�����û�����ͨ�����ֶ�����һЩ�����������ض���key��Ҫָ����ת��ҳ�棨value��
								.addExtra("androidNotification extras key", extrasparam)

								.build())
						// ָ����ǰ���͵�iOS֪ͨ
						.addPlatformNotification(IosNotification.newBuilder()
								// ��һ��IosAlert����ָ��apns title��title��subtitle��
								.setAlert(notification_title)
								// ֱ�Ӵ�alert
								// ������ָ�������͵�badge�Զ���1
								.incrBadge(1)
								// ���ֶε�ֵdefault��ʾϵͳĬ����������sound.caf��ʾ����������Ŀ��������sound.caf���������ѣ�
								// ���ϵͳû�д���Ƶ����ϵͳĬ���������ѣ����ֶ���������ַ�����iOS9�����ϵ�ϵͳ�����������ѣ����µ�ϵͳ��Ĭ������
								.setSound("sound.caf")
								// ���ֶ�Ϊ͸���ֶΣ�������ʾ��֪ͨ�����û�����ͨ�����ֶ�����һЩ�����������ض���key��Ҫָ����ת��ҳ�棨value��
								.addExtra("iosNotification extras key", extrasparam)
								// ����˵����������һ��background���ͣ����˽�background����http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
								// ȡ����ע�ͣ���Ϣ����ʱios���޷��������������
								// .setContentAvailable(true)

								.build())

						.build())
				// Platformָ������Щƽ̨�ͻ���ָ��ƽ̨�з��������������豸�������͡� jpush���Զ�����Ϣ��
				// sdkĬ�ϲ����κδ���������֪ͨ��ʾ�����鿴�ĵ�http://docs.jpush.io/guideline/faq/��
				// [֪ͨ���Զ�����Ϣ��ʲô����]�˽�֪ͨ���Զ�����Ϣ������
				.setMessage(Message.newBuilder()

						.setMsgContent(msg_content)

						.setTitle(msg_title)

						.addExtra("message extras key", extrasparam)

						.build())

				.setOptions(Options.newBuilder()
						// ���ֶε�ֵ������ָ��������Ҫ���͵�apns������false��ʾ������true��ʾ��������android���Զ�����Ϣ������
						.setApnsProduction(false)
						// ���ֶ��Ǹ��������Լ������ͱ�ţ����������߷ֱ����ͼ�¼
						.setSendno(1)
						// ���ֶε�ֵ������ָ�������͵����߱���ʱ��������������ֶ���Ĭ�ϱ���һ�죬���ָ������ʮ�죻
						.setTimeToLive(86400)

						.build())

				.build();

	}

	private static PushPayload buildPushObject_android_all_alertWithTitle(String notification_title, String msg_title,
			String msg_content) {
		System.out.println("----------buildPushObject_android_registrationId_alertWithTitle");
		return PushPayload.newBuilder()
				// ָ��Ҫ���͵�ƽ̨��all����ǰӦ�������˵�����ƽ̨��Ҳ���Դ�android�Ⱦ���ƽ̨
				.setPlatform(Platform.android())
				// ָ�����͵Ľ��ն���all���������ˣ�Ҳ����ָ���Ѿ����óɹ���tag��alias���ӦӦ�ÿͻ��˵��ýӿڻ�ȡ����registration
				// id
				.setAudience(Audience.all())
				// jpush��֪ͨ��android����jpushֱ���·���iOS����apns�������·���Winphone����mpns�·�
				.setNotification(Notification.newBuilder()
						// ָ����ǰ���͵�android֪ͨ
						.addPlatformNotification(AndroidNotification.newBuilder().setAlert(notification_title)
								.setTitle(notification_title).build())
						.build())
				// Platformָ������Щƽ̨�ͻ���ָ��ƽ̨�з��������������豸�������͡� jpush���Զ�����Ϣ��
				// sdkĬ�ϲ����κδ���������֪ͨ��ʾ�����鿴�ĵ�http://docs.jpush.io/guideline/faq/��
				// [֪ͨ���Զ�����Ϣ��ʲô����]�˽�֪ͨ���Զ�����Ϣ������
				.setMessage(Message.newBuilder().setMsgContent(msg_content).setTitle(msg_title).build())

				.setOptions(Options.newBuilder()
						// ���ֶε�ֵ������ָ��������Ҫ���͵�apns������false��ʾ������true��ʾ��������android���Զ�����Ϣ������
						.setApnsProduction(false)
						// ���ֶ��Ǹ��������Լ������ͱ�ţ����������߷ֱ����ͼ�¼
						.setSendno(1)
						// ���ֶε�ֵ������ָ�������͵����߱���ʱ��������������ֶ���Ĭ�ϱ���һ�죬���ָ������ʮ�죬��λΪ��
						.setTimeToLive(86400).build())
				.build();
	}

	private static PushPayload buildPushObject_ios_all_alertWithTitle(String notification_title, String msg_title,
			String msg_content, String extrasparam) {
		System.out.println("----------buildPushObject_ios_registrationId_alertWithTitle");
		return PushPayload.newBuilder()
				// ָ��Ҫ���͵�ƽ̨��all����ǰӦ�������˵�����ƽ̨��Ҳ���Դ�android�Ⱦ���ƽ̨
				.setPlatform(Platform.ios())
				// ָ�����͵Ľ��ն���all���������ˣ�Ҳ����ָ���Ѿ����óɹ���tag��alias���ӦӦ�ÿͻ��˵��ýӿڻ�ȡ����registration
				// id
				.setAudience(Audience.all())
				// jpush��֪ͨ��android����jpushֱ���·���iOS����apns�������·���Winphone����mpns�·�
				.setNotification(Notification.newBuilder()
						// ָ����ǰ���͵�android֪ͨ
						.addPlatformNotification(IosNotification.newBuilder()
								// ��һ��IosAlert����ָ��apns title��title��subtitle��
								.setAlert(notification_title)
								// ֱ�Ӵ�alert
								// ������ָ�������͵�badge�Զ���1
								.incrBadge(1)
								// ���ֶε�ֵdefault��ʾϵͳĬ����������sound.caf��ʾ����������Ŀ��������sound.caf���������ѣ�
								// ���ϵͳû�д���Ƶ����ϵͳĬ���������ѣ����ֶ���������ַ�����iOS9�����ϵ�ϵͳ�����������ѣ����µ�ϵͳ��Ĭ������
								.setSound("sound.caf")
								// ���ֶ�Ϊ͸���ֶΣ�������ʾ��֪ͨ�����û�����ͨ�����ֶ�����һЩ�����������ض���key��Ҫָ����ת��ҳ�棨value��
								.addExtra("iosNotification extras key", extrasparam)
								// ����˵����������һ��background���ͣ����˽�background����http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
								// .setContentAvailable(true)

								.build())
						.build())
				// Platformָ������Щƽ̨�ͻ���ָ��ƽ̨�з��������������豸�������͡� jpush���Զ�����Ϣ��
				// sdkĬ�ϲ����κδ���������֪ͨ��ʾ�����鿴�ĵ�http://docs.jpush.io/guideline/faq/��
				// [֪ͨ���Զ�����Ϣ��ʲô����]�˽�֪ͨ���Զ�����Ϣ������
				.setMessage(Message.newBuilder().setMsgContent(msg_content).setTitle(msg_title)
						.addExtra("message extras key", extrasparam).build())

				.setOptions(Options.newBuilder()
						// ���ֶε�ֵ������ָ��������Ҫ���͵�apns������false��ʾ������true��ʾ��������android���Զ�����Ϣ������
						.setApnsProduction(false)
						// ���ֶ��Ǹ��������Լ������ͱ�ţ����������߷ֱ����ͼ�¼
						.setSendno(1)
						// ���ֶε�ֵ������ָ�������͵����߱���ʱ��������������ֶ���Ĭ�ϱ���һ�죬���ָ������ʮ�죬��λΪ��
						.setTimeToLive(86400).build())
				.build();
	}

	// public static void main(String[] args){
	// if(JpushClientUtil.sendToAllIos("testIos","testIos","this is a ios Dev
	// test","")==1){
	// System.out.println("success");
	// }
	// }
}
