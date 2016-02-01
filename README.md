# AdapterRenderer
BaseAdapter 的一个简单封装，包装了控件回收的代码，极大简化了Adapter的书写，可用于ListView与GridView

## 添加到Android studio
Step1: 在根build.gradle中添加仓库：
```groovy
allprojects {
	repositories {
        jcenter()
		maven { url "https://jitpack.io" }
	}
}
```

Step2: 在工程中添加依赖：
```groovy
dependencies {
    compile 'com.github.Yumenokanata:AdapterRenderer:1.1.3'
}
```

## 基本概念
1. Renderer
  渲染器，用于渲染单行内容
2. RendererBuilder
  渲染器生成器，用于在有多种渲染方式时生成不同的渲染器

## 使用方法
一、渲染器写法
```java
//渲染器需要继承于BaseRenderer<>，其中泛型为单项内容的类型
//如在渲染器内需要Context，只需要继承ContextAware接口
public class MessageRenderer extends BaseRenderer<MessageModel> {

    TextView textview;

    //此方法用于渲染，如设置具体内容文字，由于控件会被回收，所以填充完成后此方法可能会被调用多次
    //getContent()方法可用于获取此时此项的内容
    @Override
    public void render() {
        MessageModel messageModel = getContent();
        textview.setText(messageModel.getMessageText());
    }

    //此方法用于填充，需返回一个View
    //此方法只在没有可回收控件的情况下会被调用
    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup parent) {
        return layoutInflater.inflate(R.layout.c04_list_item_user, parent, false);
    }

    //此方法中从传入的view中（即inflate方法返回的view）中获取控件
    @Override
    protected void findView(View rootView) {
        textview = (TextView)rootView.finViewById(R.id.textview)
        //ButterKnife.bind(this, rootView);
    }

    //此方法中设置监听
    @Override
    protected void setListener(View rootView) {

    }
}
```

二、Renderer的使用
2.1. 单个renderer
```java
BaseAdapter listViewAdapter = new RendererAdapter<>(
                                  new ArrayList<>(),      \\listview中的内容
                                  getContext(),           \\Context
                                  MessageRenderer.class); \\渲染类
```
                                  
2.2. 多中Renderer
2.2.1. RendererBuilder
```java
public class MessageRendererBuilder extends BaseRendererBuilder<MessageModel> {
    private List<Class<? extends BaseRenderer>> clazzList;

    public ChatListRendererBuilder(){
        clazzList = new ArrayList<>();
        clazzList.add(MessageRenderer_1.class);
        clazzList.add(MessageRenderer_2.class);
    }

    //根据传入的Model返回此Model应对应的Renderer的下标（getAllRendererClass()方法返回的list的）
    @Override
    public int getTypeClassIndex(MessageModel content) {
        return content.getSenderType() == 1 ? 1 : 0;
    }

    //返回所有渲染器class的List
    @Override
    public List<Class<? extends BaseRenderer>> getAllRendererClass() {
        return clazzList;
    }
}
```
2.2.2. 生成Adapter
```java
BaseAdapter listViewAdapter = new RendererAdapter<>(
                                  new ArrayList<>(),         \\listview中的内容
                                  getContext(),              \\Context
                                  MessageRendererBuilder()); \\RendererBuilder类的实例
```
                                  
三、生成的Adapter就和普通的BaseAdapter一样的使用方式

###License
<pre>
Copyright 2015 Yumenokanata

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
