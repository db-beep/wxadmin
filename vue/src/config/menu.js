/**
 * 菜单
 */
export default [
    {
        name: "网站首页",
        id: "0_0",
        path: "/",
    },
    {
        name: "首页",
        id: "0",
        path: "/hello"
    },
    {
        name: "功能列表",
        id: "1",
        path: "/hello/codeying",
        children: [
            {path: "/admin", name: "管理员", id: "1_0", parent: "1",roles:['',]},
            {path: "/institution", name: "非遗机构", id: "1_1", parent: "1",roles:['admin',]},
            {path: "/user", name: "用户", id: "1_2", parent: "1",roles:['admin',]},
            {path: "/intangibleCultural", name: "非遗文化科普", id: "1_3", parent: "1",roles:['institution',]},
            {path: "/news", name: "非遗资讯", id: "1_4", parent: "1",roles:['admin',]},
            {path: "/culActivi", name: "非遗传承活动", id: "1_5", parent: "1",roles:['admin','user','institution',]},
            {path: "/baom", name: "非遗活动报名", id: "1_6", parent: "1",roles:['institution','admin','user',]},
            {path: "/cultureType", name: "非遗文化类型", id: "1_7", parent: "1",roles:['admin','institution',]},
            {path: "/fankui", name: "反馈与建议", id: "1_8", parent: "1",roles:['institution','user',]},
            {path: "/notice", name: "公告", id: "1_9", parent: "1",roles:['admin','user','institution',]},
            {path: "/userComment", name: "评论", id: "1_10", parent: "1",roles:['admin',]},
        ]
    },
]

