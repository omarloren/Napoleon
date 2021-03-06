package app.trade.experts;

import app.trade.Orden;
import trade.Arithmetic;
import trade.indicator.base.Trend;

/**
 *
 * @author omar
 */
public class Inception extends Expert {
    public Double horaIni;
    public Double horaFin;
    private Double sl;
    private Double tp;
    private Integer numCoincidencias;
    private Integer velasSalida;
    private Integer contV = 0;
    private Trend trend;
    
    @Override
    public void Init() {
        Integer velasIni = this.extern.getInteger("velasIni");
        Integer velasFin = this.extern.getInteger("velasFin");
        Integer difIni = this.extern.getInteger("difIni");;
        Integer difFin = this.extern.getInteger("difFin");;
        this.setHoraIni(this.extern.getInteger("horaInicial"));
        this.setHoraFin(this.extern.getInteger("horaFinal"));
        this.velasSalida = this.extern.getInteger("VelasSalida");
        sl = Arithmetic.multiplicar(this.extern.getInteger("sl").doubleValue() , this.getPoint());
        this.tp = Arithmetic.multiplicar(this.extern.getInteger("tp").doubleValue() , this.getPoint());
        this.sl = Arithmetic.multiplicar(this.extern.getInteger("sl").doubleValue() , this.getPoint());
        this.numCoincidencias = this.extern.getInteger("numCoincidencias");;
        this.trend = this.iTrend(velasIni, velasFin, difIni, difFin, this.numCoincidencias);
    }

    @Override
    public void onTick() {
        if (this.isNewCandle()) {
            this.contV++;
            if(this.isTradeTime() && this.ordersBySymbol() < 1) {
                if(this.trend.isDn()) {
                    double stop = Arithmetic.redondear(this.getAsk() - this.sl);
                    double take = Arithmetic.redondear(this.getAsk() + this.tp);
                    this.orderSend(1.0, stop, take, '1', this.getAsk());
                    this.contV = 0;
                }else if (this.trend.isUp()) {
                    double stop = Arithmetic.redondear(this.getBid() + this.sl);
                    double take = Arithmetic.redondear(this.getBid() - this.tp);
                    this.orderSend(1.0, stop, take, '2', this.getBid());
                    this.contV = 0;
                }
            }            
        } else if (this.velasSalida > 0 && this.contV >= this.velasSalida) {
            for (int i = 0; i < this.ordersBySymbol(); i++) {
                Orden o = (Orden)this.ordersTotal(this.getMagic()).get(i);
                if(o.getSide() == '2') {
                    o.close(this.getAsk(), "Cierre por velas");
                } else if(o.getSide() == '1') {
                    o.close(this.getBid(), "Cierre por velas");
                }
            }
        }
    }

    @Override
    public void onDone() {
        
    }
}
