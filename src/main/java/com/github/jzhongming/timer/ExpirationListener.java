
package com.github.jzhongming.timer;




public interface ExpirationListener<T> {
	/** 
     * Invoking when a expired event occurs. 
     *  
     * @param expiredObject 
     */  
    void expired(T expiredObject);
}
