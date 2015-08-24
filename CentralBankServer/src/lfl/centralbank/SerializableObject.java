/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

import java.io.Serializable;

/**
 *
 * @author Luisito
 */
class SerializableObject implements Serializable{

    private Object object;

    public SerializableObject(){

    }

    public SerializableObject(Object object) {
        this.object = object;
    }

    public void setObject(Object object){
        this.object = object;
    }

    public Object getObject(){
        return object;
    }

}
