package cn.syz.scala.util

import org.junit.Test
import org.scalatest.junit.JUnitSuite

import cn.syz.scala.util.JsonStringUtils._

class JsonStringUtilsTest extends JUnitSuite {

  val validJsonArray = """[{"action":{"domain":"env","name":"current"},"device":{"type":"mobile","deviceId":{"tid":"39d08efe09a3c0de93c0f62776232dca5","imeis":["860440038463482"],"wifiMacs":[null],"androidId":"b1ca7f7a1d37e6c1","serialNo":"4104d89b"},"hardwareConfig":{"manufacture":"vivo","brand":"vivo","model":"vivo Y35A","cpuInfo":["ARMv7 Processor rev 0 (v7l)","0","Qualcomm Technologies, Inc MSM8916","null1209600\n"]},"softwareConfig":{"os":"android","osVersionName":"Android+5.0.2","osVersionCode":21,"timezone":"Asia\/Shanghai","locale":"zh_CN","timezoneInt":8}},"app":{"name":"问道","globalId":"com.gbits.atm.leiting","versionName":"0.8.0527","versionCode":201605270,"installTime":1467814833652,"updateTime":1467814833652,"uniqueId":"4A:DE:F5:AF:A9:D2:03:31:16:3E:1B:D2:DF:BC:6E:D3:47:5C:B0:3D","appKey":"6c3f2a63b372432e90b71d63562710ef","channel":"ADTrackingTEST"},"sdk":{"version":1,"minorVersion":0,"build":27,"platform":"Android"},"ts":1469994581431,"appContext":{"sessionId":"aa3f77d9-0f57-468d-afcb-adbc87a339e9","account":{"accountId":"p35qsc2t","age":0,"gender":"UNKNOWN"}},"user":{"accounts":[{"type":"sim","name":"898600e2022253521485","displayName":"460027223521485","extra1":"46002","extra2":"CMCC","extra3":"ffffffff","extra4":"cn","extra6":"860440038463482"}]},"fingerprint":"280705f1fe70545e28f7daba538b9fd0","locations":[]},{"action":{"domain":"env","name":"current"},"device":{"type":"mobile","deviceId":{"tid":"39d08efe09a3c0de93c0f62776232dca5","imeis":["860440038463482"],"wifiMacs":[null],"androidId":"b1ca7f7a1d37e6c1","serialNo":"4104d89b"},"hardwareConfig":{"manufacture":"vivo","brand":"vivo","model":"vivo Y35A","cpuInfo":["ARMv7 Processor rev 0 (v7l)","0","Qualcomm Technologies, Inc MSM8916","null1209600\n"]},"softwareConfig":{"os":"android","osVersionName":"Android+5.0.2","osVersionCode":21,"timezone":"Asia\/Shanghai","locale":"zh_CN","timezoneInt":8}},"app":{"name":"问道","globalId":"com.gbits.atm.leiting","versionName":"0.8.0527","versionCode":201605270,"installTime":1467814833652,"updateTime":1467814833652,"uniqueId":"4A:DE:F5:AF:A9:D2:03:31:16:3E:1B:D2:DF:BC:6E:D3:47:5C:B0:3D","appKey":"6c3f2a63b372432e90b71d63562710ef","channel":"ADTrackingTEST"},"sdk":{"version":1,"minorVersion":0,"build":27,"platform":"Android"},"ts":1469994761529,"appContext":{"sessionId":"aa3f77d9-0f57-468d-afcb-adbc87a339e9","account":{"accountId":"p35qsc2t","age":0,"gender":"UNKNOWN"}},"user":{"accounts":[{"type":"sim","name":"898600e2022253521485","displayName":"460027223521485","extra1":"46002","extra2":"CMCC","extra3":"ffffffff","extra4":"cn","extra6":"860440038463482"}]},"fingerprint":"7d7a1ab90eccc097478dc9bfa5f043ac","locations":[]}]"""
  val validJsonObject = """{"deviceId":"3f338a8c52c30200","appkey":"BA8ADB7EF3B7364E976F38AABB882E07","appProfile":{"versionName":"正式版v2.0.3","versionCode":"2.0.3","initTime":1481173237655,"sdkVersion":"H5+APP+v1.0.1","partner":""},"deviceProfile":{"pixel":"1080*1920*3","language":"zh-CN","timezone":8},"msgs":[{"type":2,"data":{"id":"3f338a8c52c302001481267344779000","start":1481267344782,"status":2,"duration":0,"pages":[],"events":[{"count":1,"start":1481267345976,"id":"a02非页面事件(后台到前台)","label":"a02非页面事件(后台到前台)","params":{"currentTime":"2016-12-09 15:09:05","cityId":"605","userId":"884030","chanelName":"豌豆荚"}},{"count":1,"start":1481267346356,"id":"A0d雷达页(定位成功)","label":"A0d雷达页(定位成功)","params":{"from":"lbs","cityId":"605","userId":"884030","chanelName":"豌豆荚"}}]}},{"type":2,"data":{"id":"3f338a8c52c302001481267344779000","start":1481267344782,"status":3,"duration":454,"pages":[],"events":[]}},{"type":2,"data":{"id":"3f338a8c52c302001481267347502000","start":1481267347504,"status":1,"duration":93650,"pages":[],"events":[]}}]}"""

  @Test def testSplitJsonArray(): Unit = {
    val tar = splitJsonArray(validJsonArray)
    assert(tar.length == 2)
    assert(isJsonObject(tar(0)))
    assert(isJsonObject(tar(1)))
  }

  @Test def testIsJsonArray(): Unit = {
    assert(isJsonArray(validJsonArray))
  }

  @Test def testIsJsonObject(): Unit = {
    assert(isJsonObject("""{"\"":[{}, {}, null], "{": "["}"""))
    assert(isJsonObject(validJsonObject))
  }

}