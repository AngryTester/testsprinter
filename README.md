# testsprinter
tool for speed up test works. 


##工具介绍

工具想法来源于惠普的Sprinter功能测试解决方案，但由于Sprinter过于笨重，限制较多（如必须结合ALM使用），本着开源的精神，通过Selenium WebDriver实现了Sprinter中半自动化测试的部分。

##使用说明

data.xls中的数据是操作步骤和测试数据，启动工具之后，点击"初始化浏览器"按钮，会自动打开浏览器，输入测试地址"mail.qq.com"，点击"加速"按钮，会自动点击"注册新帐户"并打开注册页面，下拉选择"注册页面"，点击"加速"按钮，会按照excel中配置的步骤和数据完成注册页面的操作。
