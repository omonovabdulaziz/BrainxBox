# Python proyektining Dockerfile

# Asosiy imkoniyatlar Docker obrazini Python 3.9.13-ga o'rnatish
FROM python:3.9.13

# Ish jarayoni /app papkasi ichida boshlanadi
WORKDIR /app

# Barcha fayllarni /app papkasiga nusxalanadi
COPY . .

# Kerakli Python paketlarini o'rnatish
RUN pip install --no-cache-dir -r requirements.txt && \
    pip install uvicorn python-multipart

# 8000-portni ochish
EXPOSE 8000

# FastAPI ilova ishga tushiriladi
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
