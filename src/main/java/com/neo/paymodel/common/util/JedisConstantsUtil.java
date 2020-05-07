package com.neo.paymodel.common.util;

/**
 * common redis key 定义类
 *
 * @author Administrator
 */
public final class JedisConstantsUtil {
    /**
     * [充值]
     * 支付APP信息信息
     * config:pay:appid:%s value : json string ()
     */
    public static final String CONFIG_PAY_APPID_KEY = "config:pay:appid:%s";

    /**
     * [充值]
     * 支付公司支付方式表渠道配置信息
     * config:pay:company:paywaycode:%s value : json string ()
     */
    public static final String CONFIG_PAY_COMPANY_PAYWAYCODE_KEY = "config:pay:company:paywaycode:%s";

    /**
     * [充值]
     * 支付公司支付方式表渠道配置信息
     * config:pay:product:code:type:%s value : json string ()
     */
    public static final String CONFIG_PAY_PRODUCT_CODE_TYPE_KEY = "config:pay:product:code:type:%s:%s";

    /**
     * 游戏大厅-玩家登录生成的TOKEN
     */
    public static final String REDIS_AUTHEN_TOKEN_FOLDER_KEY = "LoginToken:%s";
    
    /**
     * websocket-url
     */
    public static final String REDIS_WEBSOCKET_URL_KEY = "redis:websocket:url";

    /**
     * websocket-port
     */
    public static final String REDIS_WEBSOCKET_PORT_KEY = "redis:websocket:port";

    /**
     * websocket-message key
     * %user_id:%optype:%id(关联ID)
     */
    public static final String REDIS_WEBSOCKET_MESSAGE_KEY = "redis:websocket:message:%s:%s:%s";

    /**
     * 平台卡密类型 key
     * 同一个账户激活次数
     * %card_type_no:%user_id
     */
    public static final String REDIS_PAY_CARD_ACCOUNT_USED_COUNT = "redis:pay:card:account:used:%s:%s";

    /**
     * 平台卡密类型 key
     * 同一个设备激活次数
     * %card_type_no:%machine_serial
     */
    public static final String REDIS_PAY_CARD_MACHINE_SERIAL_USED_COUNT = "redis:pay:card:machine:used:%s:%s";

    /**
     * 排行榜-财富-展示条数
     */
    public static final String REDIS_RANKING_LIST_ALL_SIZE = "redis:randking:list:all:size";
    /**
     * 排行榜-财富-展示条数
     */
    public static final String REDIS_RANKING_LIST_CAIFU_SIZE = "redis:randking:list:caifu:size";
    
    /**
     * 排行榜-财富-List
     */
    public static final String REDIS_RANKING_LIST_CAIFU_LIST = "redis:randking:list:caifu:list";

    /**
     * 排行榜-赢分-展示条数
     */
    public static final String REDIS_RANKING_LIST_WIN_SIZE = "redis:randking:list:win:size";
    
    /**
     * 排行榜-今日赢金币-List
     */
    public static final String REDIS_RANKING_LIST_DAYWIN_LIST = "redis:randking:list:daywin:list";
    
    /**
     * 排行榜-今日在线时长-List
     */
    public static final String REDIS_RANKING_LIST_DAYONLINE_LIST = "redis:randking:list:dayonline:list";
    
    /**
     * 排行榜-等级-List
     */
    public static final String REDIS_RANKING_LIST_EXP_LIST = "redis:randking:list:exp:list";
    
    /**
     * 排行榜-等级-List
     */
    public static final String REDIS_RANKING_LIST_SELFINFO_LIST = "redis:randking:list:selfinfo:list";
	/**
	 * 2分钟手机[绑定支付宝]验证码
	 */
	public static final String DSGAME_PAY_MOBILE_BINDALI_VERIFY_CODE = "dsgame:pay:mobile:bindali:verify:code:%s";

	/**
	 * 2分钟手机[绑定银行卡]验证码
	 */
	public static final String DSGAME_PAY_MOBILE_BINDCARD_VERIFY_CODE = "dsgame:pay:mobile:bindcard:verify:code:%s";
	
	public static final String DSGAME_PAY_USER_INFO_CODE = "dsgame:pay:user:info:code:%s";
	
	public static final String DSGAME_PAY_USER_INFO_BACK_CODE = "dsgame:pay:user:info:back:code:%s";
	
	public static final String DSGAME_EXCHANGE_USER_VERIFY_CODE = "dsgame:exchange:user:verify:code:%s";
	
	public static final String DSGAME_PAY_SELECT_USERID_CODE = "dsgame:pay:select:userid:code:%s";
	
	public static final String DSGAME_AGENT_PAY_FLAG_CODE = "dsgame:agent:pay:flag:code";

	public static final String DSGAME_PAY_ACTIVE_FLAG = "dsgame:pay:active:flag";

	public static final String DSGAME_PAY_ACTIVE_ENDTIME = "dsgame:pay:active:endtime";
	
	public static final String DAGAME_CHATPAY_CHATID_CODE="dsgame:chatpay:chatid:code";
	
	public static final String DSGAME_AGENT_PAY_NUM_CODE = "dsgame:agent:pay:num:code";

    /**
     * 闪付
     */
    public static final String DAGAME_CHATPAY_SELECTCHATPAYLIST = "dsgame:chatpay:selectchatpaylist:%s";
    public static final String DAGAME_CHATPAY_NOTIFY = "dsgame:chatpay:notify:%s";
    public static final String DAGAME_CHATPAY_DOMAIN = "dsgame:chatpay:domain";
    public static final String DAGAME_CHATPAY_SWITCH = "dsgame:chatpay:switch";
    public static final String DAGAME_VIPPAY_SWITCH = "dsgame:vippay:switch";
    public static final String DAGAME_CHATPAY_LIMIT = "dsgame:chatpay:limit";

    /**
     * third pay
     */
    public static final String DAGAME_THIRDPAY_NOTIFY = "dsgame:thirdpay:notify:%s";
    public static final String DAGAME_THIRDPAY_TEST_SWITCH = "dsgame:thirdpay:test:switch";
    public static final String DAGAME_THIRDPAY_TEST_TMP_CONFIG = "dsgame:thirdpay:test:tmp:config";
    public static final String DAGAME_THIRDPAY_GD_ACCESS_TOKEN = "dsgame:thirdpay:gd:access:token";



    /**
     * dsgame:exchange:limit:userID:type
     */
	public static final String  DSGAME_EXCHANGE_LIMIT_USER_TYPE = "dsgame:exchange:limit:%s:%s";


    public static final String LOBBY_CONF_DNS = "lobby:conf:dns";
    public static final String LOBBY_HOTUPDATE_URL = "lobby:hotupdate:url";
    public static final String GAME_HOTUPDATE_URL = "game:hotupdate:url";
    public static final String LOBBY_REPORT_AWARD_POSTMAIL = "lobby:report:award:postmail";
    
    public static final String LOBBY_CONF_SHARE = "lobby:conf:share";
    public static final String LOBBY_CONF_CHANNEL = "lobby:conf:channel";
    public static final String LOBBY_CONF_CHANNEL_VERSION = "lobby:conf:channel:version";
    public static final String LOBBY_HIGH_VIR_IP = "lobby:high:vir:ip";
    public static final String LOBBY_HIGH_VIR_FLAG = "lobby:high:vir:flag";
    public static final String LOBBY_CONF_GAME_VERSION = "lobby:conf:game:version";
    public static final String LOBBY_CONF_PROMOTION_WX = "lobby:conf:promotion:wx";
    public static final String LOBBY_CONF_LIANYUN_CHANNEL_LIST = "lobby:conf:lianyun:channel:list";
    public static final String LOBBY_CONF_LIANYUN_CHANNEL_ONE="lobby:conf:lianyun:channel:one:%s";
    public static final String LOBBY_PAY_ACTIVE_INFO = "lobby:pay:active:info";
    /**
     * all_game_version, all_lobby_version
     */
    public static final String ALLGAME_CONF_VERSION = "allgame:conf:version";
    public static final String ALLLOBBY_CONF_VERSION = "alllobby:conf:version";

    /** 斗个地主比赛 当月 */
    public static final String DSGAME_DDZ_THEMONTH = "dsgame:ddz:compete:user:month:score:list:THEMONTH";
    public static final String DSGAME_DDZ_HOSTTHEMONTH = "dsgame:ddz:compete:user:month:score:list:THEHOSTMONTH";
    /** 斗个地主比赛 上月 */
    public static final String DSGAME_DDZ_LASTMONTH = "dsgame:ddz:compete:user:month:score:list:LASTMONTH";
    public static final String DSGAME_DDZ_HOSTLASTMONTH = "dsgame:ddz:compete:user:month:score:list:LASTHOSTMONTH";


    //Ddz
    public static final String DSGAME_DDZ_COMPETE_CONFIG = "dsgame:ddz:compete:config";
    public static final String DSGAME_DDZ_COMPETE_QRCODE= "dsgame:ddz:compete:qrcode:%s";
    public static final String DSGAME_DDZ_COMPETE_ENTER_SMS_SEND = "dsgame:ddz:compete:enter:sms:send:%s";
    public static final String DSGAME_DDZ_COMPETE_ENTER_SMS_VERIFY = "dsgame:ddz:compete:enter:sms:verify:%s";
    public static final String DSGAME_ACT_FRIEND_SHARE_LIST = "dsgame:act:frend:share:list:%s";
    public static final String DSGAME_DDZ_COMPETE_ENTER_SMS_VERIFY_STATUS = "dsgame:ddz:compete:enter:sms:verify:status:%s";

    //chuanglan
    public static final String DSGAME_CHL_TEST = "dsgame:chl:test:%s";
    
    /**
     * dsgame:lobby:activities:billboard:{serverLevel}
     */
    public static final String DSGAME_LOBBY_ACTIVITIES_BILLBOARD = "dsgame:lobby:activities:billboard:%s";

    /**
     * dsgame:lobby:activities:integral:{serverLevel}:{userId}
     */
    public static final String DSGAME_LOBBY_ACTIVITIES_INTEGRAL = "dsgame:lobby:activities:integral:%s:%s";

    public static final String DSGAME_LOBBY_ACTIVITIES_TIP = "dsgame:lobby:activities:tip";
    public static final String DSGAME_LOBBY_ACTIVITIES_CONFIG = "dsgame:lobby:activities:config";


    /**
     * 无限代理
     */
    public static final String DSGAME_TGPROMOTION_EXTRACT_USER = "dsgame:tgpromotion:extract:user:%s";
    public static final String DSGAME_TGPROMOTION_EXTRACT_LOG_USER = "dsgame:tgpromotion:extract:log:user:%s";
    public static final String DSGAME_TGPROMOTION_QRCODE_USER = "dsgame:tgpromotion:qrcode:user:%s";
    public static final String DSGAME_TGPROMOTION_DETAIL_USER = "dsgame:tgpromotion:detail:user:%s";


//    /**
//     * dsgame:lobby:billboard:{userId}
//     */
//    public static final String DSGAME_LOBBY_ACTIVITIES_BILLBOARD_USER = "dsgame:lobby:activities:b:%s";
    
    
    /**
     *	          小黑新增 渠道消息数量key
     */
    public static final String DSGAME_CHANNEL_PLAY_MESSAGE_NUM = "dsgame:channel:message:play:num";
    
    public static final String DSGAME_CHANNEL_EXCHANGE_MESSAGE_NUM = "dsgame:channel:message:exchange:num";

    
    public static final String DSGAME_EXCHANGE_ORDER_INFO_CODE = "dsgame:exchange:order:info:code:%s";
    
    /**
     * 活动
     */
    public static final String LOBBY_ACTIVE_PAY_CONFIG = "lobby:active:pay:CONFIG";
}