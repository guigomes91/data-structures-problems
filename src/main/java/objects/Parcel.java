package objects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

// 1. Definição de anotações de uso de tipo (TYPE_USE)
// Elas são necessárias para que o receiver parameter faça sentido prático.
@Target(ElementType.TYPE_USE) @interface New {}
@Target(ElementType.TYPE_USE) @interface Ordered {}
@Target(ElementType.TYPE_USE) @interface Shipped {}
@Target(ElementType.TYPE_USE) @interface Delivered {}
@Target(ElementType.TYPE_USE) @interface Cashed {}

/**
 * Exemplo prático de receiver parameter.
 */
public class Parcel {

    // O receiver parameter (ex: @New Parcel this) é puramente sintático.
    // Ele informa que este método 'order' deve ser chamado em um objeto Parcel do tipo @New.
    public void order(@New Parcel this) {
        System.out.println("Passo 1: Pacote novo sendo pedido...");
    }

    public void shipping(@Ordered Parcel this) {
        System.out.println("Passo 2: Pacote pedido está sendo enviado...");
    }

    public void deliver(@Shipped Parcel this) {
        System.out.println("Passo 3: Pacote enviado está sendo entregue...");
    }

    public void cashit(@Delivered Parcel this) {
        System.out.println("Passo 4: Pagamento do pacote entregue sendo processado...");
    }

    public void done(@Cashed Parcel this) {
        System.out.println("Passo 5: Fluxo finalizado.");
    }

    public static void main(String[] args) {
        Parcel myParcel = new Parcel();

        // No código Java, a chamada ignora o receiver parameter.
        // O compilador trata 'myParcel.order()' e 'order(myParcel)' como equivalentes internamente.
        myParcel.order();
        myParcel.shipping();
        myParcel.deliver();
        myParcel.cashit();
        myParcel.done();

        System.out.println("\nFluxo executado com sucesso!");
    }
}