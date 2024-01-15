FROM python:3.9.13
WORKDIR /app
COPY . .
RUN pip install --no-cache-dir -r requirements.txt && \
    pip install uvicorn python-multipart
EXPOSE 8000
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
