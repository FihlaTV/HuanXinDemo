package a.cool.huanxin.base;


public interface ICallback<RESULT> {

    void onResult(RESULT result);

    void onError(Throwable error);

    void onStringError(int code, String error);

}
