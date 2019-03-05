package github.com.mars_jun.utils;

import java.util.*;

public class ListUtils {
    public static void removeEmpty(List list) {
        if (list != null)
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                Object obj = it.next();
                if (obj == null)
                    it.remove();
            }
    }

    public static List removeRepeat(List args) {
        List result = new ArrayList();
        Set set = new HashSet();
        for (Iterator localIterator = args.iterator(); localIterator.hasNext(); ) {
            Object obj = localIterator.next();
            set.add(obj);
        }
        result.addAll(set);
        return result;
    }

    public static String[] removeRepeat(String[] args) {
        Set set = new HashSet();
        String[] arrayOfString1 = args;
        int j = args.length;
        for (int i = 0; i < j; i++) {
            String string = arrayOfString1[i];
            set.add(string);
        }
        String[] result = new String[set.size()];
        set.toArray(result);
        return result;
    }

    public static String splitRiskCode(List types, String appendLeft, String appendRight) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < types.size(); i++) {
            if ((i > 0) && (i < types.size())) {
                sb.append(",");
            }
            sb.append(appendLeft + types.get(i).toString() + appendRight);
        }
        return sb.toString();
    }

    public static List<List> splitList(List datas, int pageSize) {
        List result = new ArrayList();
        if ((datas != null) && (datas.size() != 0)) {
            if (pageSize != 0) {
                int pageNo = datas.size() % pageSize == 0 ? datas.size() / pageSize : datas.size() / pageSize + 1;
                for (int i = 1; i <= pageNo; i++)
                    result.add(pagination(datas, i, pageSize));
            } else {
                result.add(Boolean.valueOf(new ArrayList().addAll(datas)));
            }
        }
        return result;
    }

    public static List pagination(List datas, int pageNo, int pageSize) {
        List result = new ArrayList();
        if (datas != null) {
            int index = pageNo * pageSize <= datas.size() ? pageNo * pageSize :
                    datas.size();

            if (datas.size() != 0) {
                for (int j = (pageNo - 1) * pageSize; j < index; j++) {
                    result.add(datas.get(j));
                }
            }
        }

        return result;
    }

    public static String join(List list, String joinstr) {
        if ((list == null) || (list.size() == 0)) return "";
        return list.toString().replaceFirst("\\[|", "").replaceFirst("\\]$", "").replaceAll("\\s", "").replaceAll(",", joinstr);
    }
}
