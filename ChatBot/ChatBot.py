from flask import Flask, request, jsonify, render_template
from flask_cors import CORS 
from openai import OpenAI
from dotenv import load_dotenv
import os


load_dotenv()
app = Flask(__name__)
CORS(app)  # Enable CORS

# Initialize OpenAI client
client = OpenAI(api_key=os.environ.get("HierNichtVorhanden"))

@app.route('/')
def home():
    return render_template('index.html')  # HTML file named index.html

@app.route('/chat', methods=['POST'])
def chat():
    data = request.json
    user_message = data.get('message', '')
    print("angekommen")
    response = client.chat.completions.create(
        model="gpt-4o-mini", 
        messages=[
            {"role": "system", "content": "KontextHierEntfernt"},
            {"role": "user", "content": user_message}
        ]
    )

    bot_reply = response.choices[0].message.content.strip()
    return jsonify({'reply': bot_reply})

if __name__ == '__main__':
    app.run(debug=False)