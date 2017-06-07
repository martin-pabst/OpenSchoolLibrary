package de.sp.tools.validation;

/**
 * Created by Martin on 07.06.2017.
 */
public class ValidatorTestArray extends BaseRequestData {

    @Validation(min = 1)
    protected BaseRequestDataTestClass[] list;

    public void add(BaseRequestDataTestClass brdtc){

        int length = list == null ? 0 : list.length;

        BaseRequestDataTestClass[] list1 = new BaseRequestDataTestClass[length + 1];

        for (int i = 0; i < length; i++) {
            list1[i] = list[i];
        }

        list1[length] = brdtc;

        list = list1;
    }

}
