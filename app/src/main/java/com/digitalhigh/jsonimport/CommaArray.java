package com.digitalhigh.jsonimport;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class CommaArray {
    private static final String TAG = "CommaArray";
    private String source;
    private ArrayList<String> array;

    public CommaArray(String data) {
        this.source = data;
        String[] list = data.split(",");
        this.array = new ArrayList();

        for(int i = 0; i < list.length; ++i) {
            if(!list[i].isEmpty()) {
                this.array.add(list[i]);
            }
        }

    }

    public int size() {
        return this.source.isEmpty()?0:this.array.size();
    }

    public String get(int pos) {
        return pos >= 0?(String)this.array.get(pos):"";
    }

    public String last() {
        return this.get(this.size() - 1);
    }

    public void set(int pos, String val) {
        if(this.array.size() > pos && pos >= 0) {
            this.array.set(pos, val);
        } else if(this.array.size() == pos) {
            this.array.add(val);
        } else {
            Log.d("virtualpets:CommaArrray", "Position " + pos + " with " + val + " invalid for " + this.array.toString());
        }

    }

    public CommaArray add(String val) {
        this.array.add(val);
        return this;
    }

    public void remove(String val) {
        Iterator i = this.array.iterator();

        for(int index = 0; i.hasNext(); ++index) {
            String v = (String)i.next();
            if(v.equals(val)) {
                this.array.remove(index);
                return;
            }
        }

    }

    public boolean contains(String val) {
        return this.array.contains(val);
    }

    public void setArray(ArrayList<String> arrayList) {
        this.array = arrayList;
    }

    public String toString() {
        String out = "";

        String i;
        for(Iterator iterator = this.array.iterator(); iterator.hasNext(); out = out + i + ",") {
            i = (String)iterator.next();
        }

        return out.length() == 0?"":out.substring(0, out.length() - 1);
    }

    public String arrayToString() {
        return this.array.toString();
    }

    public long sum() {
        long sum = 0L;

        String i;
        for(Iterator iterator = this.array.iterator(); iterator.hasNext(); sum += Long.parseLong(i)) {
            i = (String)iterator.next();
            if(i.isEmpty()) {
                i = "0";
            }
        }

        return sum;
    }

    public Iterator<String> getIterator() {
        return this.array.iterator();
    }

    public void truncate(int spots) {
        if(spots < this.array.size()) {
            int trimSize = spots - this.array.size();

            for(int i = 0; i < trimSize; ++i) {
                this.array.remove(i);
            }
        }

    }

    public CommaArray getRange(int start) {
        int in;
        int out;
        if(start < 0) {
            out = this.array.size();
            in = this.array.size() - Math.abs(start);
        } else {
            out = this.array.size();
            in = start;
        }

        return this.getRange(in, out);
    }

    public CommaArray getRange(int start, int end) {
        if(start > end) {
            int template = start;
            start = end;
            end = template;
        }

        if(this.array.size() < end) {
            end = this.array.size();
        }

        if(start < 0) {
            start = 0;
        }

        Log.d("CommaArray", "Get range from " + start + " to " + end);
        CommaArray var5 = new CommaArray("");

        for(int i = start; i < end; ++i) {
            var5.add((String)this.array.get(i));
        }

        return var5;
    }

    public long average() {
        return this.size() == 0?0L:this.sum() / (long)this.size();
    }

    public long averageDelta() {
        if(this.size() == 0) {
            return 0L;
        } else {
            ArrayList deltas = new ArrayList();
            Long previous = Long.valueOf(0L);
            Long me = Long.valueOf(0L);

            for(int sum = 0; sum < this.size() - 1; ++sum) {
                previous = Long.valueOf(this.get(sum));
                me = Long.valueOf(this.get(sum + 1));
                deltas.add(Long.valueOf(me.longValue() - previous.longValue()));
            }

            Long var7 = Long.valueOf(0L);

            Long l;
            for(Iterator i$ = deltas.iterator(); i$.hasNext(); var7 = Long.valueOf(var7.longValue() + l.longValue())) {
                l = (Long)i$.next();
            }

            return var7.longValue() / (long)this.size();
        }
    }
}
