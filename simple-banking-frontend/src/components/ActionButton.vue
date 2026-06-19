<script setup>
defineProps({
  label: {
    type: String,
    required: true,
  }
})

defineEmits(["press"])
</script>

<template>
  <button type="button" class="menu-btn" @click="$emit('press')">
    {{ label }}
  </button>
</template>


<style scoped>
.menu-btn {
  padding: 12px 24px;
  font-size: 1rem;
  font-weight: 600;
  color: #ffffff;
  background-color: #1eb16f; /* Clean blue */
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease, transform 0.1s ease;
}

.menu-btn:hover {
  background-color: #157f4f;
}

.menu-btn:active {
  transform: scale(0.95);
}
</style>
