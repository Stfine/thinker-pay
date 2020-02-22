package vc.thinker.pay.alipay.helper;

import vc.thinker.pay.alipay.constants.LogisticsPayment;
import vc.thinker.pay.alipay.constants.LogisticsType;

public interface ILogistics
{
    public abstract LogisticsType getLogisticsType();
    public abstract Double getLogisticsFee();
    public abstract LogisticsPayment getLogisticsPayment();
}
