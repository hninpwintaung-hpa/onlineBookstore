/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Luisito
 */
public class CentralBankProxy implements InvocationHandler{

    Object obj;

    public CentralBankProxy(Object o){
        obj = o;
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result = null;
        try {
            // Do something before the method is called ...
            /*System.out.println("I'm here in the BankProxy invoke...");
            System.out.println("About to invoke method: " + m.getName());
            System.out.println("obj is class: " + obj.getClass().getName());
            System.out.println("proxy is class: " + proxy.getClass().getName());*/

            result = m.invoke((CentralBankImpl)obj, args);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            // Do something after the method is called ...
        }
        return result;
    }

}
