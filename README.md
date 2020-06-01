ScaleView
--------------
### Android 刻度尺（标尺）自定义View。

![Demo效果](https://raw.githubusercontent.com/ChiuLui/ScaleView/master/image/RulerView_0.gif)


![某项目内效果](https://raw.githubusercontent.com/ChiuLui/ScaleView/master/image/RulerView_1.gif)


### 可实现不同效果。

##### View 自定义属性：

|属性|作用|
|:------:|:------:|
| name="min_index" format="integer" | 最小刻度 |
| name="max_index" format="integer" | 最大刻度 |
| name="now_index" format="integer" | 当前刻度 |
| name="scale_value" format="integer" | 每格刻度的值 |
| name="text_size" format="float" | 刻度数值的字体大小 |
| name="pointer_width" format="float" | 指针线宽度 |
| name="pointer_head" format="float" | 指针头宽度 |
| name="pointer_top" format="boolean" | 指针是否向上 |
| name="pointer_top_protruding" format="boolean" | 指针是否向上突出 |
| name="pointer_bottom_protruding" format="boolean" | 指针是否向下突出(相对于刻度线来说，如下对齐情况中，指针线下面要长于刻度线) |
| name="show_pointer_head" format="boolean" | 是否显示针头 |
| name="baseLine_width" format="float" | 底线线宽度 |
| name="low_scale_width" format="float" | 低刻度线宽度 |
| name="middle_scale_width" format="float" | 中刻度线宽度 |
| name="high_scale_width" format="float" | 高刻度线宽度 |
| name="line_interval" format="integer" | 刻度与刻度之间的距离 |
| name="baseLine_margin_bottom" format="integer" | 所有元素（除了文字）与 View 底的距离（下边距为文字留位置） |
| name="pointer_margin_top" format="integer" | 所有元素（除了文字）与 View 顶的距离（上边距为文字留位置） |
| name="left_margin_left" format="integer" | 所有元素（除了文字）与  View 左边的距离 |
| name="right_margin_right" format="integer" | 所有元素（除了文字）与  View 右边的距离 |
| name="font_margin_bottom" format="integer" | 文字下边距（避免显示不全） |
| name="font_margin_top" format="integer" | 文字上边距（避免显示不全） |
| name="low_pointer_margin" format="integer" | 低刻度的边距（控制长度） |
| name="middle_pointer_margin" format="integer" | 中刻度的边距（控制长度） |
| name="high_pointer_margin" format="integer" | 高刻度的边距（控制长度） |
| name="middle_frequency" format="integer" | 中刻度线频率 |
| name="high_frequency" format="integer" | 高刻度线频率 |
| name="baseLine_color" format="color" | 底线颜色 |
| name="high_scale_color" format="color" | 高刻度线颜色 |
| name="middle_scale_color" format="color" | 中刻度线颜色 |
| name="low_scale_color" format="color" | 低刻度线颜色 |
| name="num_color" format="color" | 刻度数值颜色 |
| name="pointer_color" format="color" | 指针颜色 |
| name="font_top" format="boolean" | 字体是否要绘制在上面 |
| name="scale_ratio" format="float" | 显示的刻度数字与刻度比例（比如要显示小数的情况）：刻度 / 比例 = 显示刻度 |
| name="sliding_ratio" format="float" | 滑动距离比例（用于调整滑动速度）：刻度间距离 * 滑动速度比例 = 每滑动多少距离改变状态 |
| name="show_baseLine" format="boolean" | 是否显示底线 |
| name="units" format="float" | 惯性滑动速率时间单位, 多少毫秒时间单位内运动了多少个像素 |
| name="max_velocity" format="float" | 最大滑动数率 |
| name="strokeCap" format="enum" | Paint 的线帽: BUTT:无线帽，ROUND：圆角， SQUARE：矩形 |
| name="scale_position" format="enum" | 刻度线对齐：top：上对齐，center：居中对齐，bottom：下对齐 |


##### 方法：

|方法|作用|
|:------:|:------:|
| setNowIndex(double) | 设置刻度 |
| setPlusScale() | 加刻度 |
| setMinusScale() | 减刻度 |
| setMaxIndex(double) | 设置最小刻度范围 |
| setMaxIndex(double) | 设置最大刻度范围 |


##### 监听：

|监听|作用|
|:------:|:------:|
| setOnScaleChangeListener(OnScaleChangeListener) | 设置改变刻度回调监听 |