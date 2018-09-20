package com.example.rxtest.mvp.presenter;

import com.example.rxtest.mvp.bean.MovieInfo;
import com.example.rxtest.mvp.model.OperatorInterface;
import com.example.rxtest.mvp.model.OperatorInterfaceImp;
import com.example.rxtest.mvp.view.OperatorView;
import com.example.rxtest.widget.LoadingDialog;

/**
 * Created by Administrator on 2018/8/27.
 */

public class GetMoviePresenter {
    private final OperatorView operatorView;
    OperatorInterface operatorInterface;

    public GetMoviePresenter(OperatorView operatorView) {
        this.operatorView = operatorView;
        operatorInterface = new OperatorInterfaceImp();
    }

    /*获取电影信息*/
    public void getMovieInfo(final LoadingDialog loadingDialog) {
        loadingDialog.show();
        operatorInterface.requset(operatorView.getStart(), operatorView.getCount(), new OperatorInterface.OnRequestListener() {
            @Override
            public void success(MovieInfo movieInfo) {
                operatorView.showRequestMsg(movieInfo);
                loadingDialog.dismiss();
            }

            @Override
            public void failed(Throwable s) {
                loadingDialog.dismiss();
                operatorView.showRequestFailMsg(s);
            }
        });
    }
}
