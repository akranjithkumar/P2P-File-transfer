from flask import Flask, jsonify, request, render_template
from flask_cors import CORS
import socket
import qrcode
import os
import atexit

# Ensure socket is closed on app exit
atexit.register(lambda: sock.close())

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# Server address and port
HOST = '0.0.0.0'  # Listen on all interfaces
PORT = 2345  # Port to bind the server

# Create a UDP socket to handle file transfer
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((HOST, PORT))

# Function to generate QR code with network details (IP and PORT)
def generate_qr_code():
    ip = socket.gethostbyname(socket.gethostname())
    qr_data = f"http://{ip}:{PORT}/"
    img = qrcode.make(qr_data)
    img.save("static/qr_code.png")

# Route to serve the QR code image
@app.route('/')
def index():
    generate_qr_code()  # Generate QR on accessing the root URL
    return render_template('index.html')

# Route to handle file transfer (receive file via UDP)
@app.route('/send_file', methods=['POST'])
def send_file():
    with open("received_file", "wb") as f:
        print("Receiving file...")
        while True:
            data, addr = sock.recvfrom(1024)  # Receive file data
            if not data:
                break
            f.write(data)
    return jsonify({"message": "File received successfully."})

if __name__ == "__main__":
    app.run(debug=True, host=HOST)
