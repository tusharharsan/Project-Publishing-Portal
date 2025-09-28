import os

from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer

import torch

# Login to HF (make sure your token is set in environment or replace directly)
# login("hf_your_token_here")

# Load model
hf_token = os.getenv("HF_TOKEN")
model = SentenceTransformer("google/embeddinggemma-300m", use_auth_token=hf_token)

app = Flask(__name__)

@app.route("/rank", methods=["POST"])
def rank_documents():
    try:
        data = request.json
        query = data.get("query")
        documents = data.get("documents", [])

        if not query or not documents:
            return jsonify({"error": "query and documents are required"}), 400

        # Encode query and docs
        query_embedding = model.encode(query, convert_to_tensor=True)
        doc_embeddings = model.encode(documents, convert_to_tensor=True)

        # Compute similarities (cosine similarity)
        similarities = torch.nn.functional.cosine_similarity(
            query_embedding.unsqueeze(0), doc_embeddings
        )

        # Return as list
        ranked = [
            {"document": doc, "score": float(score)}
            for doc, score in zip(documents, similarities)
        ]

        # Sort by score
        ranked = sorted(ranked, key=lambda x: x["score"], reverse=True)

        return jsonify({"results": ranked})

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(debug=True, port=5000)
