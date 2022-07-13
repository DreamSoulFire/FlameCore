package dream.soulflame.flamecore.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum TabList {
    FIRST_ARG(Arrays.asList("change", "check", "get", "help",
            "reload", "run", "set", "toggle"),0,null,new int[]{1}),
    CHANGE_CHILDREN(Collections.singletonList("指定的玩家(可不填) 要转移的第一个槽位 要转移的第二个槽位"),1,"change",new int[]{2}),
    CHECK_CHILDREN(Arrays.asList("id", "material"),1,"check",new int[]{2}),
    GET_CHILDREN(Collections.singletonList("指定的玩家(可不填) 指定的龙核槽位"),1,"get",new int[]{2}),
    RUN_CHILDREN(Collections.singletonList("要执行指令组的玩家 指令组id"),1,"run",new int[]{2}),
    SET_CHILDREN(Collections.singletonList("指定的玩家(可不填) 指定的龙核槽位"),1,"set",new int[]{2});
    private final List<String> list;//返回的List
    private final int[] num;//这个参数可以出现的位置
    private final int befPos;//识别的上个参数的位置
    private final String bef;//上个参数的内容

    TabList(List<String> list,int befPos, String bef, int[] num){
        this.list = list;
        this.befPos = befPos;
        this.bef = bef;
        this.num = num.clone();
    }

    public List<String> getList() {
        return list;
    }

    public int[] getNum() {
        return num;
    }

    public int getBefPos() {
        return befPos;
    }

    public String getBef() {
        return bef;
    }

    public static List<String> returnList(String[] Para, int curNum) {
        for(TabList tab : TabList.values()) {
            if(tab.getBefPos() - 1 >= Para.length) continue;
            if((tab.getBef() == null ||
                    tab.getBef().equalsIgnoreCase(Para[tab.getBefPos() - 1])) &&
                    Arrays.binarySearch(tab.getNum(),curNum) >= 0) return tab.getList();
        }
        return null;
    }
}
