### 四大组件之广播

> Android 应用与 Android 系统和其他 Android 应用之间可以相互收发广播消息，这与发布-订阅设计模式相似。
> 这些广播会在所关注的事件发生时发送。举例来说，Android 系统会在发生各种系统事件时发送广播，例如系统启动或设备开始充电时。
> 再比如，应用可以发送自定义广播来通知其他应用它们可能感兴趣的事件（例如，一些新数据已下载）。
> 应用可以注册接收特定的广播。广播发出后，系统会自动将广播传送给同意接收这种广播的应用。

#### 接收广播

应用可以通过两种方式接收广播：清单声明的接收器和上下文注册的接收器。

- 清单声明的接收器

要在清单中声明广播接收器，请执行以下步骤：

1. 在应用清单中指定`receiver`元素。

   ```xml
       <receiver android:name=".MyBroadcastReceiver"  android:exported="true">
           <intent-filter>
               <action android:name="android.intent.action.BOOT_COMPLETED"/>
               <action android:name="android.intent.action.INPUT_METHOD_CHANGED" />
           </intent-filter>
       </receiver>
   ```

   Intent 过滤器指定您的接收器所订阅的广播操作。

2. 创建 `BroadcastReceiver` 子类并实现 `onReceive(Context, Intent)`。以下示例中的广播接收器会记录并显示广播的内容：

   ```java
       public class MyBroadcastReceiver extends BroadcastReceiver {
               private static final String TAG = "MyBroadcastReceiver";
               @Override
               public void onReceive(Context context, Intent intent) {
                   StringBuilder sb = new StringBuilder();
                   sb.append("Action: " + intent.getAction() + "\n");
                   sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
                   String log = sb.toString();
                   Log.d(TAG, log);
                   Toast.makeText(context, log, Toast.LENGTH_LONG).show();
               }
           }
       
   ```

   系统软件包管理器会在应用安装时注册接收器。然后，该接收器会成为应用的一个独立入口点，这意味着如果应用当前未运行，系统可以启动应用并发送广播。

   系统会创建新的 `BroadcastReceiver` 组件对象来处理它接收到的每个广播。此对象仅在调用 `onReceive(Context, Intent)` 期间有效。一旦从此方法返回代码，系统便会认为该组件不再活跃。

- 上下文注册的接收器

  要使用上下文注册接收器，请执行以下步骤：

  1. 创建 `BroadcastReceiver` 的实例。

     ```java
     BroadcastReceiver br = new MyBroadcastReceiver();
     ```

  2. 创建 `IntentFilter` 并调用 `registerReceiver(BroadcastReceiver, IntentFilter)` 来注册接收器：

     ```java
     IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
     filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
     this.registerReceiver(br, filter);
     ```

     只要注册上下文有效，上下文注册的接收器就会接收广播。例如，如果您在 `Activity` 上下文中注册，只要 Activity 没有被销毁，您就会收到广播。如果您在应用上下文中注册，只要应用在运行，您就会收到广播。

  3. 要停止接收广播，请调用 `unregisterReceiver(android.content.BroadcastReceiver)`。当您不再需要接收器或上下文不再有效时，请务必注销接收器。

     请注意注册和注销接收器的位置，比方说，如果您使用 Activity 上下文在 `onCreate(Bundle)` 中注册接收器，则应在 `onDestroy()` 中注销，以防接收器从 Activity 上下文中泄露出去。如果您在 `onResume()` 中注册接收器，则应在 `onPause()` 中注销，以防多次注册接收器（如果您不想在暂停时接收广播，这样可以减少不必要的系统开销）。请勿在 `onSaveInstanceState(Bundle)` 中注销，因为如果用户在历史记录堆栈中后退，则不会调用此方法。

#### 发送广播

Android 为应用提供三种方式来发送广播：

- `sendOrderedBroadcast(Intent, String)` 方法一次向一个接收器发送广播。当接收器逐个顺序执行时，接收器可以向下传递结果，也可以完全中止广播，使其不再传递给其他接收器。接收器的运行顺序可以通过匹配的 intent-filter 的 android:priority 属性来控制；具有相同优先级的接收器将按随机顺序运行。
- `sendBroadcast(Intent)` 方法会按随机的顺序向所有接收器发送广播。这称为常规广播。这种方法效率更高，但也意味着接收器无法从其他接收器读取结果，无法传递从广播中收到的数据，也无法中止广播。
- `LocalBroadcastManager.sendBroadcast` 方法会将广播发送给与发送器位于同一应用中的接收器。如果您不需要跨应用发送广播，请使用本地广播。这种实现方法的效率更高（无需进行进程间通信），而且您无需担心其他应用在收发您的广播时带来的任何安全问题。

#### 使用

##### 监听电量变化

```java
/**
 * 创建一个广播接收器
 */
private class BroadcastReceiver extends android.content.BroadcastReceiver{
   @Override
   public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG,"收到电量变化的广播 --- "+action);
   }
}
```

```java
// 初始化频道
IntentFilter intentFilter = new IntentFilter();
// 添加我们要监听的频道
intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
// 注册广播
this.registerReceiver(new BroadcastReceiver(),intentFilter);
```

##### 通过广播接收者显示电池电量