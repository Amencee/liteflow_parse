package com.example.liteflowParse.core.el;

import com.yomahub.liteflow.builder.el.ELWrapper;
import com.yomahub.liteflow.builder.el.ThenELWrapper;
import com.yomahub.liteflow.builder.el.WhenELWrapper;

public class FlowConvertELUtil {

    public static void convert(ELWrapper wrapper, ELWrapper elWrapper) {
        if (wrapper instanceof ThenELWrapper) {
            ThenELWrapper thenELWrapper = (ThenELWrapper) wrapper;
            thenELWrapper.then(elWrapper);
        } else if (wrapper instanceof WhenELWrapper) {
            WhenELWrapper whenELWrapper = (WhenELWrapper) wrapper;
            whenELWrapper.when(elWrapper);
        }
    }



    public static void convert(ELWrapper wrapper, Object array) {
        if (wrapper instanceof ThenELWrapper) {
            ThenELWrapper thenELWrapper = (ThenELWrapper) wrapper;
            thenELWrapper.then(array);
        } else if (wrapper instanceof WhenELWrapper) {
            WhenELWrapper whenELWrapper = (WhenELWrapper) wrapper;
            whenELWrapper.when(array);
        }
    }


}
