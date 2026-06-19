<script setup>
import { computed } from 'vue';
import ActionButton from './ActionButton.vue';

const props = defineProps({
  role: { type: String, required: true }
})

defineEmits(['menuSelect'])

// Single source of truth for which role sees which buttons.
// Mirrors the @RolesAllowed matrix on the auth service.
const ALL_ACTIONS = [
  { id: 'GET_ALL',          label: 'Get All Users',    roles: ['ADMIN', 'TELLER'] },
  { id: 'GET_USER',         label: 'Get User Details', roles: ['ADMIN', 'TELLER', 'CUSTOMER'] },
  { id: 'ADD_USER',         label: 'Add User',         roles: ['ADMIN'] },
  { id: 'DELETE_USER',      label: 'Delete User',      roles: ['ADMIN'] },
  { id: 'CHANGE_BALANCE',   label: 'Change Balance',   roles: ['ADMIN', 'TELLER'] },
  { id: 'TRANSFER_BALANCE', label: 'Transfer Balance', roles: ['ADMIN', 'TELLER', 'CUSTOMER'] },
]

const visibleActions = computed(() =>
  ALL_ACTIONS.filter((action) => action.roles.includes(props.role))
)
</script>

<template>
  <nav>
    <ActionButton
      v-for="action in visibleActions"
      :key="action.id"
      :label="action.label"
      @press="$emit('menuSelect', action.id)"
    />
  </nav>
</template>

<style scoped>
  nav {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    align-items: center;
    justify-content: center;
  }
</style>
