package es.deusto.prog3.captureflag.controller;

// interfaz utilizada por el controlador del
// juego para avisar de eventos
public interface GameListener {
    
    // avisa cuando se recoge una bandera
    public void flagPicked();

    // avisa cuando se recogen todas las banderas
    public void gameFinished();
}
