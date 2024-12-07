package packageObserver;

import java.util.ArrayList;
import java.util.List;

public class NotificacionManager {
    private List<Observer> observers = new ArrayList<>();

    //Agregar observadores
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Eliminar observadores (probar despues)
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Notificar
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}