<script setup>
import { ref } from 'vue'

const props = defineProps({
  action: { type: String, required: true }
})

const emit = defineEmits(['close', 'submit'])

// Local tracking states for form data fields
const accountNumber = ref('')
const fullName = ref('')
const phoneNumber = ref('')
const initialBalance = ref(0)
const amount = ref(0)
const balanceActionType = ref('increase')
const targetAccountNumber = ref('')
const password = ref('')
const role = ref('CUSTOMER')

// Helper function to generate a random 10-digit numeric string
const generateAccountNumber = () => {
  let result = ''
  for (let i = 0; i < 10; i++) {
    result += Math.floor(Math.random() * 10).toString()
  }
  return result
}

const handleFormSubmit = () => {
  // 1. GET_USER or DELETE_USER
  if (props.action === 'GET_USER' || props.action === 'DELETE_USER') {
    emit('submit', { accountNumber: accountNumber.value })
  }

  // 2. ADD_USER — includes login credentials now that auth reads user_entities
  else if (props.action === 'ADD_USER') {
    emit('submit', {
      name: fullName.value,
      accountNumber: generateAccountNumber(),
      phoneNumber: phoneNumber.value,
      balance: Number(initialBalance.value),
      password: password.value,
      role: role.value
    })
  }

  // 3. CHANGE_BALANCE
  else if (props.action === 'CHANGE_BALANCE') {
    emit('submit', {
      accountNumber: accountNumber.value,
      action: balanceActionType.value,
      amount: Number(amount.value)
    })
  }

  // 4. TRANSFER_BALANCE
  else if (props.action === 'TRANSFER_BALANCE') {
    emit('submit', {
      sourceAccountNumber: accountNumber.value,
      targetAccountNumber: targetAccountNumber.value,
      amount: Number(amount.value)
    })
  }
}
</script>

<template>
  <div class="modal-overlay">
    <div class="modal-box">
      <h2>Action: {{ action.replace('_', ' ') }}</h2>

      <div v-if="['GET_USER', 'DELETE_USER', 'CHANGE_BALANCE'].includes(action)" class="input-group">
        <label>Account Number:</label>
        <input v-model="accountNumber" type="text" placeholder="e.g., 9876543210" />
      </div>

      <div v-if="action === 'ADD_USER'" class="input-group">
        <label>Full Name:</label>
        <input v-model="fullName" type="text" placeholder="Ali Farhan" />

        <label>Phone Number:</label>
        <input v-model="phoneNumber" type="tel" placeholder="e.g., 00923324444555" />

        <label>Initial Balance:</label>
        <input v-model="initialBalance" type="number" step="0.01" placeholder="100000.0" />

        <label>Password:</label>
        <input v-model="password" type="password" placeholder="min 6 characters" />

        <label>Role:</label>
        <select v-model="role" class="select-input">
          <option value="CUSTOMER">Customer</option>
          <option value="TELLER">Teller</option>
          <option value="ADMIN">Admin</option>
        </select>
      </div>

      <div v-if="action === 'CHANGE_BALANCE'" class="input-group">
        <label>Adjustment Type:</label>
        <select v-model="balanceActionType" class="select-input">
          <option value="increase">Increase Balance (+)</option>
          <option value="decrease">Decrease Balance (-)</option>
        </select>

        <label>Amount:</label>
        <input v-model="amount" type="number" step="0.01" min="0" placeholder="e.g., 500.00" />
      </div>

      <template v-if="action === 'TRANSFER_BALANCE'">
        <div class="input-group">
          <label>Source Account Number</label>
          <input v-model="accountNumber" type="text" placeholder="Enter source account" required />
        </div>
        <div class="input-group">
          <label>Target Account Number</label>
          <input v-model="targetAccountNumber" type="text" placeholder="Enter target account" required />
        </div>
        <div class="input-group">
          <label>Transfer Amount</label>
          <input v-model="amount" type="number" min="1" placeholder="0.00" required />
        </div>
      </template>

      <div class="modal-actions">
        <button @click="$emit('close')" class="btn-cancel">Cancel</button>
        <button @click="handleFormSubmit" class="btn-submit">Submit to API</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.75);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-box {
  background: #1e2530;
  color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  width: 450px;
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

input,
.select-input {
  padding: 11px;
  border: 1px solid #334155;
  border-radius: 6px;
  font-size: 1rem;
  outline: none;
  background-color: #111827;
  color: #ffffff;
  transition: border-color 0.2s, box-shadow 0.2s;
}

input:focus,
.select-input:focus {
  border-color: #1eb16f;
  box-shadow: 0 0 0 2px rgba(56, 189, 248, 0.2);
}

.select-input option {
  background-color: #1e2530;
  color: #ffffff;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 25px;
}

button {
  padding: 11px 20px;
  border: none;
  border-radius: 6px;
  font-size: 0.95rem;
  cursor: pointer;
  font-weight: bold;
  transition: opacity 0.2s, background-color 0.2s;
}

.btn-cancel {
  background: #334155;
  color: #cbd5e1;
}

.btn-cancel:hover {
  background: #475569;
}

.btn-submit {
  background: #1eb16f;
  color: #111827;
}

.btn-submit:hover {
  background: #157f4f;
}
</style>
