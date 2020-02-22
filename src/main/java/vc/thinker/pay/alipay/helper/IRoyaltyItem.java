package vc.thinker.pay.alipay.helper;

public interface IRoyaltyItem
{
    public abstract String getPayAccount();

    public abstract String getAccount();
    public abstract Double getAmount();
    public abstract String getMemo();
}
