package me.evrooij.groceries.data;

import java.util.LinkedList;
import java.util.List;

public class ExampleDataProvider {
    private List<ProductData> mData;
    private ProductData mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public ExampleDataProvider(List<Product> products) {

        mData = new LinkedList<>();

        for (Product p : products) {
            final long id = mData.size();
            final int viewType = 0;
            mData.add(new ProductData(id, viewType, p));
        }
    }

    public int getCount() {
        return mData.size();
    }

    public ProductData getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return mData.get(index);
    }

    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }

            mData.add(insertedPosition, mLastRemovedData);

            mLastRemovedData = null;
            mLastRemovedPosition = -1;

            return insertedPosition;
        } else {
            return -1;
        }
    }

    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final ProductData removedItem = mData.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }

    public static final class ProductData {

        private final long mId;
        private final Product mProduct;
        private final int mViewType;

        ProductData(long id, int viewType, Product product) {
            mId = id;
            mViewType = viewType;
            mProduct = product;
        }

        public int getViewType() {
            return mViewType;
        }

        public long getId() {
            return mId;
        }

        public Object getItem() {
            return mProduct;
        }
    }
}
