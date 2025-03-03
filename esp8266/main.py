import network
import socket
import machine
import time
from machine import Pin, PWM

# WiFi credentials
WIFI_SSID = "Rakshith_5G"
WIFI_PASSWORD = "Living1life"

# GPIO pin mappings
LED_PINS = {
    "blue1": 23,
    "blue2": 22,
    "red1": 1,
    "red2": 3,
    "green1": 21,
    "green2": 19
}

# Initialize PWM pins
pwms = {name: PWM(Pin(pin)) for name, pin in LED_PINS.items()}
for pwm in pwms.values():
    pwm.freq(1000)
    pwm.duty(0)

def connect_wifi():
    sta_if = network.WLAN(network.STA_IF)
    if not sta_if.isconnected():
        print('Connecting to WiFi...')
        sta_if.active(True)
        sta_if.connect(WIFI_SSID, WIFI_PASSWORD)
        while not sta_if.isconnected():
            time.sleep(0.1)
    print('Network config:', sta_if.ifconfig())

def handle_request(client):
    request = client.recv(1024).decode()
    print("Request:", request)
    
    # Parse request
    if "GET /control" in request:
        params = request.split('?')[1].split(' ')[0]
        params = dict(p.split('=') for p in params.split('&'))
        
        # Update PWM values
        for led, value in params.items():
            if led in pwms:
                pwms[led].duty(int(value))
        
        client.send("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\nOK")
    else:
        client.send("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\nNot Found")
    client.close()

def start_webserver():
    addr = socket.getaddrinfo('0.0.0.0', 80)[0][-1]
    sock = socket.socket()
    sock.bind(addr)
    sock.listen(1)
    print('Listening on', addr)
    
    while True:
        client, addr = sock.accept()
        try:
            handle_request(client)
        except Exception as e:
            print("Error:", e)
            client.close()

def main():
    connect_wifi()
    start_webserver()

if __name__ == "__main__":
    main()
