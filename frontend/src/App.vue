<script setup>
import { ref, onMounted } from "vue";
import api from "./services/api";

const email = ref("");
const password = ref("");
const token = ref(localStorage.getItem("token"));
const message = ref("");

const leads = ref([]);
const from = ref("");
const to = ref("");

const newName = ref("");
const newEmail = ref("");
const newPhone = ref("");
const newSource = ref("");

const editingId = ref(null);
const editName = ref("");
const editEmail = ref("");
const editPhone = ref("");
const editSource = ref("");

const loading = ref(false);

const login = async () => {
  try {
    loading.value = true;

    const res = await api.post("/api/auth/login", {
      email: email.value,
      password: password.value,
    });
    localStorage.setItem("token", res.data.token);
    token.value = res.data.token;
    fetchLeads();
  } catch {
    message.value = "Login failed";
  } finally {
    loading.value = false;
  }
};

const fetchLeads = async () => {
  try {
    loading.value = true;

    const params = {};
    if (from.value) params.from = from.value;
    if (to.value) params.to = to.value;

    const res = await api.get("/api/leads", { params });
    leads.value = res.data;
  } finally {
    loading.value = false;
  }
};

const createLead = async () => {
  try {
    loading.value = true;

    await api.post("/api/leads", {
      name: newName.value,
      email: newEmail.value,
      phone: newPhone.value,
      source: newSource.value
    });
    newName.value = "";
    newEmail.value = "";
    newSource.value = "";
    newPhone.value = "";
    fetchLeads();
  } finally {
    loading.value = false;
  }
};

const startEdit = (lead) => {
  editingId.value = lead.id;
  editName.value = lead.name;
  editEmail.value = lead.email;
  editPhone.value = lead.phone;
  editSource.value = lead.source;
};

const cancelEdit = () => {
  editingId.value = null;
};

const updateLead = async (id) => {
  try {
    loading.value = true;

    await api.put(`/api/leads/${id}`, {
      name: editName.value,
      email: editEmail.value,
      phone: editPhone.value,
      source: editSource.value,
    });

    editingId.value = null;
    fetchLeads();
  } finally {
    loading.value = false;
  }
};

const deleteLead = async (id) => {
  try {
    loading.value = true;

    if (!confirm("Are you sure you want to delete this lead?")) return;

    await api.delete(`/api/leads/${id}`);
    fetchLeads();
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  if (token.value) fetchLeads();
});
</script>

<template>
  <div class="container">
    <div v-if="loading" class="loader-overlay">
      <div class="spinner"></div>
    </div>
    <h2 v-if="!token">Login</h2>

    <div v-if="!token">
      <input v-model="email" placeholder="Email" />
      <input v-model="password" type="password" />
      <button @click="login">Login</button>
      <p>{{ message }}</p>
    </div>

    <div v-else>
      <h2>Leads</h2>

      <div class="filters">
        <input type="date" v-model="from" />
        <input type="date" v-model="to" />
        <button @click="fetchLeads">Filter</button>
      </div>

      <div class="create">
        <input v-model="newName" placeholder="Name" />
        <input v-model="newEmail" placeholder="Email" />
        <input v-model="newPhone" placeholder="Phone Number" />
        <input v-model="newSource" placeholder="Source" />
        <button @click="createLead">Create Lead</button>
      </div>

      <table class="leads-table" v-if="leads.length">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Source</th>
            <th>Created</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="lead in leads" :key="lead.id">
            <template v-if="editingId === lead.id">
              <td><input v-model="editName" /></td>
              <td><input v-model="editEmail" /></td>
              <td><input v-model="editPhone" /></td>
              <td><input v-model="editSource" /></td>
              <td>{{ new Date(lead.created_at).toLocaleDateString() }}</td>
              <td>
                <button @click="updateLead(lead.id)">Save</button>
                <button @click="cancelEdit">Cancel</button>
              </td>
            </template>

            <template v-else>
              <td>{{ lead.name }}</td>
              <td>{{ lead.email }}</td>
              <td>{{ lead.phone }}</td>
              <td>{{ lead.source }}</td>
              <td>{{ new Date(lead.created_at).toLocaleDateString() }}</td>
              <td>
                <button @click="startEdit(lead)">Edit</button>
                <button @click="deleteLead(lead.id)">Delete</button>
              </td>
            </template>
          </tr>
        </tbody>
      </table>

      <p v-else>No leads found.</p>
    </div>
  </div>
</template>

<style>
.container {
  max-width: 600px;
  margin: 40px auto;
  font-family: Arial, sans-serif;
}

input {
  margin: 5px;
}

button {
  margin-right: 5px;
}

.leads-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

.leads-table th,
.leads-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.leads-table th {
  background-color: #f4f4f4;
}

.loader-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #ddd;
  border-top: 4px solid #333;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
