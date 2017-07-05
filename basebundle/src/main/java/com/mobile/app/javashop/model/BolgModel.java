package com.mobile.app.javashop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.nat.android.javashoplib.netutils.BaseData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LDD on 17/3/28.
 */
public class BolgModel extends BaseData {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String getMessage() {
        return data!=null&&data.size()!=0?"请求成功！":"请求失败！";
    }

    @Override
    public int getResult() {
        return data!=null&&data.size()!=0?1:0;
    }

    public static class DataBean implements Parcelable {
        /**
         * title : LoadRunner常用技巧
         * date : 2017-03-27T07:30:46.000Z
         * path : 2017/03/27/1/
         * text : 脚本在录制脚本中，采用Loadrunner而自动录制脚本难免会录入一切不必要的数据，如果直接采用这个样的脚本进行压测的话，测试结果会十分不准确，因为在LoadRunner的录制过程中，包括Cookies，不必要的Url都会被录制下来，所以我们需要去删减脚本，增强脚本。 脚本越小越好主要包括：删除cookies、删除关键字EXTRARS后面的url、删除不必要的url、删除一切带有敏感标记的内容（比如：不想压测到实际生产线上时，则需要删除www.*.com的所有相关内容） 脚本增强脚本录制完成之后，通过脚本回放可以关联一些动态的参数。在脚本中找到动态参数，右键Replace with parameter（替换为动态参数），根据弹框出现的相应步骤添加动态参数的数据。 自定义参数： “Select next row ”选项中有以下几种选择：多个VU如何取值 Sequential：按照顺序一行行的读取。每一个虚拟用户都会按照相同的顺序读取 Random：在每次循环里随机的读取一个，但是在循环中一直保持不变 Unique ：每个VU取唯一的值。注意：使用该类型必须注意数据表有足够多的数。比如Controller中设定20 个虚拟用户进行5 次循环，那么编号为1 的虚拟用户取前5个数，编号为2 的虚拟用户取6-10的数，依次类推，这样数据表中至少要有100个数据，否则Controller 运行过程中会返回一个错误。 事务在压力测试中，对于性能的衡量，恒大一本分都是根据事务处理的TPS来衡量，所以在事务的设置中，我们要尽量的合理安排。事务要尽可能的只保留核心逻辑。 迭代与并发迭代；在运行时设置中打开step，在step中设置具体的step。并发要在Controller中设置要同时并发多少虚拟用户，可以根据自己的需求，去设置相应的压测方案！ 错误扫雷这里记录几个常遇到的错误解决办法，下载超时，连接超时，发送数据超时，发生这几个错误时，一般做这样的处理，就可以解决：
         * content : <h3 id="脚本"><a href="#脚本" class="headerlink" title="脚本"></a>脚本</h3><p><code>在录制脚本中，采用Loadrunner而自动录制脚本难免会录入一切不必要的数据，如果直接采用这个样的脚本进行压测的话，测试结果会十分不准确，因为在LoadRunner的录制过程中，包括Cookies，不必要的Url都会被录制下来，所以我们需要去删减脚本，增强脚本。</code></p>
         <h4 id="脚本越小越好"><a href="#脚本越小越好" class="headerlink" title="脚本越小越好"></a>脚本越小越好</h4><p><code>主要包括：删除cookies、删除关键字EXTRARS后面的url、删除不必要的url、删除一切带有敏感标记的内容（比如：不想压测到实际生产线上时，则需要删除www.*.com的所有相关内容）</code></p>
         <h4 id="脚本增强"><a href="#脚本增强" class="headerlink" title="脚本增强"></a>脚本增强</h4><p><code>脚本录制完成之后，通过脚本回放可以关联一些动态的参数。在脚本中找到动态参数，右键Replace with parameter（替换为动态参数），根据弹框出现的相应步骤添加动态参数的数据。
         自定义参数：
         “Select next row ”选项中有以下几种选择：多个VU如何取值
         Sequential：按照顺序一行行的读取。每一个虚拟用户都会按照相同的顺序读取
         Random：在每次循环里随机的读取一个，但是在循环中一直保持不变
         Unique ：每个VU取唯一的值。注意：使用该类型必须注意数据表有足够多的数。比如Controller中设定20 个虚拟用户进行5 次循环，那么编号为1 的虚拟用户取前5个数，编号为2 的虚拟用户取6-10的数，依次类推，这样数据表中至少要有100个数据，否则Controller 运行过程中会返回一个错误。</code></p>
         <h3 id="事务"><a href="#事务" class="headerlink" title="事务"></a>事务</h3><p><code>在压力测试中，对于性能的衡量，恒大一本分都是根据事务处理的TPS来衡量，所以在事务的设置中，我们要尽量的合理安排。事务要尽可能的只保留核心逻辑。</code></p>
         <h3 id="迭代与并发"><a href="#迭代与并发" class="headerlink" title="迭代与并发"></a>迭代与并发</h3><p><code>迭代；在运行时设置中打开step，在step中设置具体的step。并发要在Controller中设置要同时并发多少虚拟用户，可以根据自己的需求，去设置相应的压测方案！</code><br><img src="http://onghqryqs.bkt.clouddn.com/Image/jpg%E5%B9%B6%E5%8F%91.png?imageView2/0/q/75|watermark/2/text/WXVNZW5nU2h1YWlfQmxvZw==/font/5qW35L2T/fontsize/600/fill/I0QzRDBEMA==/dissolve/100/gravity/SouthEast/dx/10/dy/10|imageslim" alt=""></p>
         <h3 id="错误扫雷"><a href="#错误扫雷" class="headerlink" title="错误扫雷"></a>错误扫雷</h3><p><code>这里记录几个常遇到的错误解决办法，下载超时，连接超时，发送数据超时，发生这几个错误时，一般做这样的处理，就可以解决：</code><br><img src="http://onghqryqs.bkt.clouddn.com/Image/jpg%E9%94%99%E8%AF%AF.png?imageView2/0/q/75|watermark/2/text/WXVNZW5nU2h1YWlfQmxvZw==/font/5qW35L2T/fontsize/600/fill/I0QzRDBEMA==/dissolve/100/gravity/SouthEast/dx/10/dy/10|imageslim" alt=""></p>

         * comments : true
         * link :
         * tags : [{"name":"LoadRunner","slug":"LoadRunner","permalink":"https://yumengshuaii.github.io/tags/LoadRunner/"}]
         */

        private String title;
        private String date;
        private String path;
        private String text;
        private String content;
        private boolean comments;
        private String link;
        private List<TagsBean> tags;



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isComments() {
            return comments;
        }

        public void setComments(boolean comments) {
            this.comments = comments;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }


        public static class TagsBean implements Parcelable {
            /**
             * name : LoadRunner
             * slug : LoadRunner
             * permalink : https://yumengshuaii.github.io/tags/LoadRunner/
             */

            private String name;
            private String slug;
            private String permalink;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public String getPermalink() {
                return permalink;
            }

            public void setPermalink(String permalink) {
                this.permalink = permalink;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.slug);
                dest.writeString(this.permalink);
            }

            public TagsBean() {
            }

            protected TagsBean(Parcel in) {
                this.name = in.readString();
                this.slug = in.readString();
                this.permalink = in.readString();
            }

            public static final Creator<TagsBean> CREATOR = new Creator<TagsBean>() {
                @Override
                public TagsBean createFromParcel(Parcel source) {
                    return new TagsBean(source);
                }

                @Override
                public TagsBean[] newArray(int size) {
                    return new TagsBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.date);
            dest.writeString(this.path);
            dest.writeString(this.text);
            dest.writeString(this.content);
            dest.writeByte(this.comments ? (byte) 1 : (byte) 0);
            dest.writeString(this.link);
            dest.writeList(this.tags);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.title = in.readString();
            this.date = in.readString();
            this.path = in.readString();
            this.text = in.readString();
            this.content = in.readString();
            this.comments = in.readByte() != 0;
            this.link = in.readString();
            this.tags = new ArrayList<TagsBean>();
            in.readList(this.tags, TagsBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
