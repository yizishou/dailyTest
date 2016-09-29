package cn.syz.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SaveMode

object T2 {

  //  val schema = StructType(
  //    StructField("col1", ArrayType(ArrayType(LongType)))
  //      :: Nil)

  val schema = StructType(
    StructField("mDeviceId", StringType, false) ::
      StructField("mDeveploperAppkey", StringType, false) ::
      StructField("mAppProfile", StructType(
        StructField("mAppPackageName", StringType, false) ::
          StructField("mAppVersionName", StringType, false) ::
          StructField("mAppVersionCode", StringType, false) ::
          StructField("mStartTime", LongType, false) ::
          StructField("mSdkVersion", StringType, false) ::
          StructField("mPartnerId", StringType, false) ::
          StructField("isCracked", BooleanType, true) ::
          StructField("installationTime", LongType, false) ::
          StructField("purchaseTime", LongType, false) ::
          StructField("appStoreID", LongType, true) :: Nil), false) ::
      StructField("mDeviceProfile", StructType(
        StructField("mMobileModel", StringType, false) ::
          StructField("mOsSdkVersion", StringType, false) ::
          StructField("mGis", StructType(
            StructField("lng", DoubleType, false) ::
              StructField("lat", DoubleType, false) :: Nil), false) ::
          StructField("mCpuABI", StringType, false) ::
          StructField("mPixelMetric", StringType, false) ::
          StructField("mCountry", StringType, false) ::
          StructField("mCarrier", StringType, false) ::
          StructField("mLanguage", StringType, false) ::
          StructField("mTimezone", IntegerType, false) ::
          StructField("mOsVersion", StringType, false) ::
          StructField("mChannel", IntegerType, false) ::
          StructField("m2G_3G", StringType, false) ::
          StructField("isJailBroken", BooleanType, true) ::
          StructField("mSimOperator", StringType, true) ::
          StructField("mNetworkOperator", StringType, false) ::
          StructField("hostName", StringType, false) ::
          StructField("deviceName", StringType, false) ::
          StructField("kernBootTime", LongType, false) ::
          StructField("advertisingID", StringType, false) ::
          StructField("mWifiBSSID", StringType, true) ::
          StructField("mMobileNetType", StringType, true) ::
          StructField("mCellID", IntegerType, true) ::
          StructField("mLac", IntegerType, true) :: Nil), false) ::
      StructField("mTMessages",
        ArrayType(
          StructType(
            StructField("mMsgType", IntegerType, false) ::
              StructField("session",
                StructType(
                  StructField("id", StringType, false) ::
                    StructField("start", LongType, false) ::
                    StructField("mStatus", IntegerType, false) ::
                    StructField("duration", IntegerType, false) ::
                    StructField("activities",
                      ArrayType(
                        StructType(
                          StructField("name", StringType, false) ::
                            StructField("start", LongType, false) ::
                            StructField("duration", IntegerType, false) ::
                            StructField("refer", StringType, false) ::
                            StructField("type", IntegerType, true) :: Nil), true), false) ::
                      StructField("appEvents",
                        ArrayType(
                          StructType(
                            StructField("id", StringType, false) ::
                              StructField("label", StringType, false) ::
                              StructField("count", IntegerType, false) ::
                              StructField("startTime", LongType, true) ::
                              StructField("parameters",
                                ArrayType(
                                  StructType(
                                    StructField("key", StringType, true) ::
                                      StructField("value", NullType, true) :: Nil), true), true) :: Nil), true), false) ::
                        StructField("isConnected", IntegerType, true) :: Nil), false) ::
                StructField("mInitProfile", StructType(StructField("mCpuDiscription", StringType, false) ::
                  StructField("mCpuCoreNum", IntegerType, false) ::
                  StructField("mCpuFrequency", FloatType, false) ::
                  StructField("mCpuImplementor", StringType, false) ::
                  StructField("mGpuVendor", StringType, false) ::
                  StructField("mGpuRenderer", StringType, false) ::
                  StructField("mMemoryTotal", IntegerType, false) ::
                  StructField("mMemoryFree", IntegerType, false) ::
                  StructField("mMobileStorageTotal", IntegerType, false) ::
                  StructField("mMobileStorageFree", IntegerType, false) ::
                  StructField("mSDCardStorageTotal", IntegerType, false) ::
                  StructField("mSDCardStorageFree", IntegerType, false) ::
                  StructField("mBatteryCapacity", IntegerType, false) ::
                  StructField("mDisplaMetricWidth", FloatType, false) ::
                  StructField("mDisplaMetricHeight", FloatType, false) ::
                  StructField("mDisplayMetricDensity", IntegerType, false) ::
                  StructField("mRomInfo", StringType, false) ::
                  StructField("mBaseBand", StringType, false) ::
                  StructField("mIMEI", StringType, false) ::
                  StructField("mMACAddress", StringType, false) ::
                  StructField("mApnName", StringType, false) ::
                  StructField("mApn_mcc", StringType, false) ::
                  StructField("mApn_mnc", StringType, false) ::
                  StructField("mApn_proxy", BooleanType, false) ::
                  StructField("mIMSI", StringType, false) ::
                  StructField("mUpid", StringType, false) ::
                  StructField("mSimId", StringType, false) :: Nil), false) ::
                StructField("mAppException", StructType(StructField("mErrorTime", LongType, false) ::
                  StructField("mRepeat", IntegerType, false) ::
                  StructField("mAppVersionCode", StringType, false) ::
                  StructField("data", BinaryType, false) ::
                  StructField("mShortHashCode", StringType, true) :: Nil), false) :: Nil), true), false) ::
        StructField("activeApps", ArrayType(ArrayType(LongType, true), true), false) ::
        StructField("mActiveAppInfoIOS", StringType, false) ::
        StructField("ip", StringType, false) ::
        StructField("headers", ArrayType(StructType(StructField("key", StringType, true) :: StructField("value", StringType, true) :: Nil), true), false) :: Nil)

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "test", new SparkConf())
    val ssc = new SQLContext(sc)
    //    val row = Row(Seq(Seq(1L, 2L), Seq(2L, 3L, 4L)))
    val row = Row(Seq())
    val rowRDD = sc.makeRDD(Seq(row))
    val df = ssc.createDataFrame(rowRDD, schema)
    df.show
    df.write.mode(SaveMode.Overwrite).json("D:/tmp/test.json")
    df.write.mode(SaveMode.Overwrite).parquet("D:/tmp/test.parquet")
    sc.stop
  }

}