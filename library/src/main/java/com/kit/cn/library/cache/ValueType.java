package com.kit.cn.library.cache;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * @author zhouwen
 * @version 1.0
 * @since 2016/6/27 10:28
 */
public enum ValueType implements IValueAble<Integer> {

    STRING{
        @Override
        public String getName() {
            return "string";
        }

        @Override
        public Integer getValue() {
            return this.ordinal();
        }
    }, BITMAP{
        @Override
        public String getName() {
            return "bitmap";
        }

        @Override
        public Integer getValue() {
            return this.ordinal();
        }
    }
}
