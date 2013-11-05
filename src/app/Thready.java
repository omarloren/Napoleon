package app;

import app.trade.Gear;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import help.Crono;
import java.util.Map;
import util.Settings;

/**
 *
 * @author omar
 */
public class Thready implements Runnable{
    private Gear gear;
    private DBCursor data;
    private static int cont = 0;
    public Thready(Settings settings, Map<String, Object> it, Integer from, Integer _break, Integer to){
        this.gear = new Gear(settings, it, from, _break, to);
        cont++;
        this.gear.setId(cont);
    }
    
    public void setData(DBCursor data){
        this.data = data;
    }
    
    @Override
    public void run(){
        System.err.println("#"+this.gear.id + " iniciando :)");
        Crono c = new Crono();
        c.init();
        while (this.data.hasNext()) {
            DBObject o = this.data.next();
            this.gear.tick(o);
            //System.out.println(this.gear.id+ " => " + o );
       }
        System.err.println("#"+this.gear.id + " has finished => "+ c.end());
    }
    
}
