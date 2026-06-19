<!-- App.vue -->
<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import LoginForm from './components/LoginForm.vue'
import ActionMenu from './components/ActionMenu.vue'
import FormModal from './components/FormModal.vue'
import ResponseModal from './components/ResponseModal.vue'

const GATEWAY = 'http://localhost:8081/gateway'
const BASE_URL = `${GATEWAY}/users`

// Every request gets a hard timeout so a downstream service that never
// responds surfaces as a clear error instead of an endless spinner.
const REQUEST_TIMEOUT_MS = 15000
const api = axios.create({ timeout: REQUEST_TIMEOUT_MS })

// ---- Session state ----
// Credentials live only in memory; the Basic header is rebuilt from them
// on every request. No more hardcoded admin token.
const session = ref(null) // { accountNumber, role, authHeader }
const loginError = ref('')
const loginLoading = ref(false)

// ---- UI state ----
const activeAction = ref('')
const showForm = ref(false)
const showResponse = ref(false)
const apiResult = ref('')

const headers = computed(() => ({
  'Authorization': session.value?.authHeader,
  'Content-Type': 'application/json'
}))

const handleLogin = async ({ accountNumber, password }) => {
  loginError.value = ''
  loginLoading.value = true
  const authHeader = 'Basic ' + btoa(`${accountNumber}:${password}`)

  try {
    const response = await api.get(`${GATEWAY}/login`, {
      headers: { 'Authorization': authHeader }
    })
    // Auth service confirms identity + role: { accountNumber, role }
    session.value = {
      accountNumber: response.data.accountNumber,
      role: response.data.role,
      authHeader
    }
  } catch (error) {
    loginError.value =
      error.response?.status === 401
        ? 'Invalid account number or password.'
        : `Login failed: ${error.message}`
  } finally {
    loginLoading.value = false
  }
}

const handleLogout = () => {
  session.value = null
  showForm.value = false
  showResponse.value = false
}

const handleMenuClick = (actionName) => {
  activeAction.value = actionName
  if (actionName === 'GET_ALL') {
    // No input object needed, fire request directly
    executeActualApiCall()
  } else if (actionName === 'GET_USER' && session.value?.role !== 'ADMIN') {
    // Non-admins may only view their own account, so skip the prompt and
    // look up the logged-in account directly.
    executeActualApiCall({ accountNumber: session.value.accountNumber })
  } else {
    showForm.value = true
  }
}

// Catches the dynamic payload packet object from FormModal
const executeActualApiCall = async (formData = null) => {
  showForm.value = false
  apiResult.value = 'Contacting Gateway Service...'
  showResponse.value = true

  try {
    let response;

    switch (activeAction.value) {
      case 'GET_ALL':
        response = await api.get(BASE_URL, { headers: headers.value })
        break

      case 'GET_USER':
        response = await api.get(`${BASE_URL}/${formData.accountNumber}`, { headers: headers.value })
        break

      case 'DELETE_USER':
        response = await api.delete(`${BASE_URL}/${formData.accountNumber}`, { headers: headers.value })
        break

      case 'CHANGE_BALANCE':
        response = await api.patch(`${BASE_URL}/balance`, formData, { headers: headers.value })
        break

      case 'ADD_USER':
        response = await api.post(BASE_URL, formData, { headers: headers.value })
        break

      case 'TRANSFER_BALANCE':
        response = await api.post(`${BASE_URL}/transfer`, formData, { headers: headers.value })
        break
    }

    apiResult.value = JSON.stringify(response.data, null, 2)

  } catch (error) {
    if (error.code === 'ECONNABORTED') {
      apiResult.value =
        'Request timed out. Check that the gateway (8081), auth (8082) and core (8080) services are all running.'
    } else {
      apiResult.value = error.response?.data || error.message
    }
  }
}
</script>


<template>
  <div class="app-layout">
    <h1>API Gateway Dashboard</h1>

    <!-- Not logged in: show only the login card -->
    <LoginForm
      v-if="!session"
      :error-message="loginError"
      :loading="loginLoading"
      @login="handleLogin"
    />

    <!-- Logged in: role-filtered menu -->
    <template v-else>
      <div class="session-bar">
        <span class="session-info">
          Account <strong>{{ session.accountNumber }}</strong>
          &mdash; role <span class="role-badge">{{ session.role }}</span>
        </span>
        <button class="btn-logout" @click="handleLogout">Logout</button>
      </div>

      <ActionMenu :role="session.role" @menuSelect="handleMenuClick" />

      <FormModal
        v-if="showForm"
        :action="activeAction"
        @close="showForm = false"
        @submit="executeActualApiCall"
      />

      <ResponseModal
        v-if="showResponse"
        :result="apiResult"
        @close="showResponse = false"
      />
    </template>
  </div>
</template>


<style scoped>
.app-layout {
  font-family: sans-serif;
  text-align: center;
  padding: 40px;
}

.session-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 900px;
  margin: 0 auto 30px;
  padding: 12px 18px;
  background: #1e2530;
  border: 1px solid #2d3748;
  border-radius: 8px;
  color: #94a3b8;
  font-size: 0.95rem;
}

.session-info strong {
  color: #ffffff;
}

.role-badge {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 999px;
  background: #1eb16f;
  color: #111827;
  font-weight: 700;
  font-size: 0.8rem;
}

.btn-logout {
  padding: 8px 16px;
  border: 1px solid #334155;
  border-radius: 6px;
  background: transparent;
  color: #cbd5e1;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.btn-logout:hover {
  background: #334155;
}
</style>
