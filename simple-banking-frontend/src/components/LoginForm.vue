<script setup>
import { ref } from 'vue'

defineProps({
  errorMessage: { type: String, default: '' },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['login'])

const accountNumber = ref('')
const password = ref('')

const submit = () => {
  if (!accountNumber.value || !password.value) return
  emit('login', { accountNumber: accountNumber.value, password: password.value })
}
</script>

<template>
  <div class="login-card">
    <h2>Sign In</h2>

    <div class="input-group">
      <label>Account Number</label>
      <input
        v-model="accountNumber"
        type="text"
        placeholder="e.g., 1000000001"
        @keyup.enter="submit"
      />
    </div>

    <div class="input-group">
      <label>Password</label>
      <input
        v-model="password"
        type="password"
        placeholder="••••••••"
        @keyup.enter="submit"
      />
    </div>

    <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>

    <button class="btn-login" :disabled="loading" @click="submit">
      {{ loading ? 'Verifying...' : 'Login' }}
    </button>
  </div>
</template>

<style scoped>
.login-card {
  background: #1e2530;
  color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  width: 380px;
  margin: 60px auto 0;
  border: 1px solid #2d3748;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.5);
  text-align: left;
}

h2 {
  margin-top: 0;
  margin-bottom: 20px;
  font-family: sans-serif;
  font-size: 1.3rem;
  color: #1eb16f;
  border-bottom: 1px solid #2d3748;
  padding-bottom: 10px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 15px 0;
}

label {
  font-size: 0.9rem;
  font-weight: 600;
  color: #94a3b8;
  font-family: sans-serif;
}

input {
  padding: 11px;
  border: 1px solid #334155;
  border-radius: 6px;
  font-size: 1rem;
  outline: none;
  background-color: #111827;
  color: #ffffff;
  transition: border-color 0.2s, box-shadow 0.2s;
}

input:focus {
  border-color: #1eb16f;
  box-shadow: 0 0 0 2px rgba(56, 189, 248, 0.2);
}

.error-text {
  color: #f87171;
  font-size: 0.9rem;
  font-family: sans-serif;
  margin: 10px 0 0;
}

.btn-login {
  width: 100%;
  margin-top: 20px;
  padding: 12px;
  border: none;
  border-radius: 6px;
  background: #1eb16f;
  color: #111827;
  font-size: 0.95rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.2s ease, opacity 0.2s ease;
}

.btn-login:hover {
  background: #157f4f;
}

.btn-login:disabled {
  opacity: 0.6;
  cursor: wait;
}
</style>
