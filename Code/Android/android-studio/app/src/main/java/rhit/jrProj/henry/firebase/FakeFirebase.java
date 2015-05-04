package rhit.jrProj.henry.firebase;

import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hullzr on 4/23/2015.
 */
public class FakeFirebase {

    String uRL;
    public ArrayList<String> pushesToFakeFirebase;
    public HashMap<String, Object> map;

    public FakeFirebase(){
        this.uRL = "blah";
        this.pushesToFakeFirebase = new ArrayList<String>();
        this.map = new HashMap<String, Object>();
    }



    public String toString(){
        return this.uRL;
    }

    public void push(String toPush){
        this.pushesToFakeFirebase.add(toPush);

    }

    public void setValue(HashMap<String, Object> map){
        this.map = map;
    }


}
