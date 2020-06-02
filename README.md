
ScaleView
-------------

# 简介:
> 🌟适用于Android的刻度尺（标尺）控件。🔥高度自定义🔥内存占用低，纯绘制，不使用动画。

# 目录:
[1.实现事例](#1)

[2.自定义属性](#2)

[3.方法](#3)

[4.监听](#4)


# <span id = "1">**1.实现事例**</span>

### 默认样式:

![Demo效果](https://github.com/ChiuLui/ScaleView/blob/master/image/RulerView_0.gif)

```

<ScaleView
        android:id="@+id/scaleView"
        android:layout_width="match_parent"
        android:layout_height="100dp">


```



### 某项目内样式:

![某项目内效果](https://github.com/ChiuLui/ScaleView/blob/master/image/RulerView_1.gif)

```

<ScaleView
        android:id="@+id/scaleView"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        app:min_index="0"
        app:max_index="300"
        app:now_index="50"
        app:show_pointer_head="false"
        app:scale_position="top"
        app:pointer_margin_top="30"
        app:font_top="false"
        app:show_baseLine="false"
        app:pointer_top_protruding="true"
        app:high_scale_color="#C7C7C7"
        app:middle_scale_color="#C7C7C7"
        app:low_scale_color="#C7C7C7"
        app:num_color="#01C5C6"
        app:pointer_color="#FF8282"
        app:low_scale_width="3"
        app:middle_scale_width="3"
        app:high_scale_width="3"
        app:pointer_width="3"
        app:low_pointer_margin="150"
        app:middle_pointer_margin="100"
        app:high_pointer_margin="50" />


```



### 显示负数、向下视图、隐藏底线样式:

![某项目内效果](https://github.com/ChiuLui/ScaleView/blob/master/image/RulerView_2.gif)


```

<ScaleView
        android:id="@+id/scaleView"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        app:min_index="-100"
        app:max_index="100"
        app:now_index="0"
        app:show_baseLine="false"
        app:pointer_top="false"
        app:scale_position="center"
        app:font_top="false"
        app:pointer_margin_top="0"/>


```



### 显示整数、居中视图、隐藏指针头样式:

![某项目内效果](https://github.com/ChiuLui/ScaleView/blob/master/image/RulerView_3.gif)


```

<ScaleView
        android:id="@+id/scaleView"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        app:min_index="-100"
        app:max_index="100"
        app:now_index="0"
        app:show_baseLine="false"
        app:scale_position="center"
        app:scale_ratio="1"
        app:baseLine_margin_bottom="0"
        app:show_pointer_head="false" />


```



# <span id = "2">**2.自定义属性**</span>

|属性|作用|默认值|
|:-----|:-----|:----:|
| name="min_index" format="integer" | 最小刻度 | 0 |
| name="max_index" format="integer" | 最大刻度 | 300 |
| name="now_index" format="integer" | 当前刻度 | 150 |
| name="scale_value" format="integer" | 每格刻度的值 | 1 |
| name="text_size" format="float" | 刻度数值的字体大小 | 50f |
| name="pointer_width" format="float" | 指针线宽度 | 3f |
| name="pointer_head" format="float" | 指针头宽度 | 30 |
| name="pointer_top" format="boolean" | 指针是否向上 | true |
| name="pointer_top_protruding" format="boolean" | 指针是否向上突出 | false |
| name="pointer_bottom_protruding" format="boolean" | 指针是否向下突出(相对于刻度线来说，如下对齐情况中，指针线下面要长于刻度线) | false |
| name="show_pointer_head" format="boolean" | 是否显示针头 | true |
| name="baseLine_width" format="float" | 底线线宽度 | 5f |
| name="low_scale_width" format="float" | 低刻度线宽度 | 1f |
| name="middle_scale_width" format="float" | 中刻度线宽度 | 2f |
| name="high_scale_width" format="float" | 高刻度线宽度 | 3f |
| name="line_interval" format="integer" | 刻度与刻度之间的距离 | 30 |
| name="baseLine_margin_bottom" format="integer" | 所有元素（除了文字）与 View 底的距离（下边距为文字留位置） | 50 |
| name="pointer_margin_top" format="integer" | 所有元素（除了文字）与 View 顶的距离（上边距为文字留位置） | 50 |
| name="left_margin_left" format="integer" | 所有元素（除了文字）与  View 左边的距离 | 0 |
| name="right_margin_right" format="integer" | 所有元素（除了文字）与  View 右边的距离 | 0 |
| name="font_margin_bottom" format="integer" | 文字下边距（避免显示不全） | 0 |
| name="font_margin_top" format="integer" | 文字上边距（避免显示不全） | 40 |
| name="low_pointer_margin" format="integer" | 低刻度的边距（控制长度） | 150 |
| name="middle_pointer_margin" format="integer" | 中刻度的边距（控制长度） | 100 |
| name="high_pointer_margin" format="integer" | 高刻度的边距（控制长度） | 50 |
| name="middle_frequency" format="integer" | 中刻度线频率 | 5 |
| name="high_frequency" format="integer" | 高刻度线频率 | 10 |
| name="baseLine_color" format="color" | 底线颜色 | R.color.colorPrimaryDark |
| name="high_scale_color" format="color" | 高刻度线颜色 | R.color.colorEA4335 |
| name="middle_scale_color" format="color" | 中刻度线颜色 | R.color.color3379F6 |
| name="low_scale_color" format="color" | 低刻度线颜色 | R.color.colorDEB8B1 |
| name="num_color" format="color" | 刻度数值颜色 | R.color.colorPrimary |
| name="pointer_color" format="color" | 指针颜色 | R.color.colorAccent |
| name="font_top" format="boolean" | 字体是否要绘制在上面 | true |
| name="scale_ratio" format="float" | 显示的刻度数字与刻度比例（比如要显示小数的情况）：刻度 / 比例 = 显示刻度 | 10 |
| name="sliding_ratio" format="float" | 滑动距离比例（用于调整滑动速度）：刻度间距离 * 滑动速度比例 = 每滑动多少距离改变状态 | 0.5 |
| name="show_baseLine" format="boolean" | 是否显示底线 | true |
| name="units" format="float" | 惯性滑动速率时间单位, 多少毫秒时间单位内运动了多少个像素 | 500 |
| name="max_velocity" format="float" | 最大滑动数率 | 15000 |
| name="strokeCap" format="enum" | Paint 的线帽: BUTT:无线帽，ROUND：圆角， SQUARE：矩形 | SQUARE |
| name="scale_position" format="enum" | 刻度线对齐：top：上对齐，center：居中对齐，bottom：下对齐 | bottom |


# <span id = "3">**3.方法**</span>

|方法|作用|
|:-----|:-----|
| setNowIndex(double) | 设置刻度 |
| setPlusScale() | 加刻度 |
| setMinusScale() | 减刻度 |
| setMaxIndex(double) | 设置最小刻度范围 |
| setMaxIndex(double) | 设置最大刻度范围 |




# <span id = "4">**4.监听**</span>

|监听|作用|
|:-----|:-----|
| setOnScaleChangeListener(OnScaleChangeListener) | 设置改变刻度回调监听 |

