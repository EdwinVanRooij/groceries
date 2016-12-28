package me.evrooij.groceries.data;

import java.util.LinkedList;
import java.util.List;

public class ProductDataProvider {
    private List<ProductData> productDataList;
    private ProductData lastRemovedProductData;
    private int mLastRemovedPosition = -1;

    public ProductDataProvider(List<Product> products) {

        productDataList = new LinkedList<>();

        for (Product p : products) {
            final long id = productDataList.size();
            final int viewType = 0;
            productDataList.add(new ProductData(id, viewType, p));
        }
    }

    public int getCount() {
        return productDataList.size();
    }

    public ProductData getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return productDataList.get(index);
    }

    public int undoLastRemoval() {
        if (lastRemovedProductData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < productDataList.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = productDataList.size();
            }

            productDataList.add(insertedPosition, lastRemovedProductData);

            lastRemovedProductData = null;
            mLastRemovedPosition = -1;

            return insertedPosition;
        } else {
            return -1;
        }
    }

    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final ProductData removedItem = productDataList.remove(position);

        lastRemovedProductData = removedItem;
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
