package com.xygame.sg.utils.assort;
/***
 * 分类接口，根据V value返回K key
 *
 * @param <K>
 * @param <V>
 */
public interface KeySort<K, V> {
	K getKey(V v);
}
