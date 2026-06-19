<script setup>
import { computed } from 'vue'

const props = defineProps({
  result: { type: String, required: true }
})

defineEmits(['close'])

// Replaces any raw textual string "\n" characters with true operational newline line breaks
const formattedResult = computed(() => {
  if (!props.result) return ''
  return props.result
})
</script>

<template>
  <div class="modal-overlay">
    <div class="modal-box">
      <h2>Server Log Outcome</h2>

      <pre class="terminal">{{ formattedResult }}</pre>

      <button @click="$emit('close')" class="btn-close">Dismiss Window</button>
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
  background: rgba(0, 0, 0, 0.75); /* Dark background overlay tint */
  backdrop-filter: blur(8px);       /* High visibility glass blur effect */
  -webkit-backdrop-filter: blur(8px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1100; /* Stays above your main form input viewports */
}

.modal-box {
  background: #1e2530; /* Uniform dark layout backdrop */
  color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  width: 50vw;
  border: 1px solid #2d3748;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.5);
  text-align: left;
}

h2 {
  margin-top: 0;
  margin-bottom: 20px;
  font-family: sans-serif;
  font-size: 1.3rem;
  color: #1eb16f; /* Cyan header accent color style matching */
  border-bottom: 1px solid #2d3748;
  padding-bottom: 10px;
}

.terminal {
  background: #111827; /* Code block inset terminal look */
  color: #4af626;      /* Matrix terminal green text */
  padding: 15px;
  border-radius: 6px;
  font-family: 'Courier New', Courier, monospace;
  font-size: 0.95rem;
  line-height: 1.6;
  border-left: 4px solid #1eb16f;
  margin-bottom: 25px;

  white-space: pre-wrap; /* Maintains string layout breaking patterns */
  word-break: break-word;
  overflow-wrap: break-word;
}

.btn-close {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 6px;
  background: #1eb16f;
  color: #111827;
  font-size: 0.95rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.2s ease;
  text-align: center;
}

.btn-close:hover {
  background: #157f4f;
}
</style>
