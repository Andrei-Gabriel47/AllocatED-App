import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api', // modifică dacă backendul tău e pe alt port
  headers: {
    'Content-Type': 'application/json'
  }
});

export default axiosInstance;