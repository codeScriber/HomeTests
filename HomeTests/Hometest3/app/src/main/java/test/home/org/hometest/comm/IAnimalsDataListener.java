package test.home.org.hometest.comm;

import java.util.List;

import test.home.org.hometest.data.AnimalData;

/**
 * Created by Eyal on 02-2-15.
 */
public interface IAnimalsDataListener {
    public void onAnimalsDataresult(int errorcode, List<AnimalData> parsedDataLeft, List<AnimalData> parsedDataRight);
}
