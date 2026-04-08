# Blog App

## Features

- Create, edit, delete, and view blog posts.
- Like and unlike posts from the details page.
- Category tags per post (comma-separated input in form).
- Client-side routing for home, post details, create, and edit pages.
- Form validation for required fields (title, categories, content).
- Toast notifications for key actions (create, update, delete, like/unlike).
- Post data persistence in browser `localStorage`.

## Public URL

- Azure App Service: https://blog-app-3221178-39368.azurewebsites.net

## Local Setup Steps

```bash
npm install
npm run dev
```

Open the URL shown by Vite (usually http://localhost:5173).

Optional:

```bash
npm run build
npm run preview
npm run lint
```

## Redux vs Context Usage

This app uses both Redux and Context, each with a clear role:

- **Redux (application/domain state)**
	- Files: `src/features/posts/postsSlice.js`, `src/app/store.js`
	- Manages post CRUD and like toggle actions.
	- Persists posts to `localStorage` via store subscription (`savePostsToStorage`).

- **Context (UI state)**
	- Files: `src/context/UiContext.jsx`, `src/context/uiContext.js`
	- Manages transient toast messages and UI-only behavior.

Why this split:

- Redux provides predictable centralized state updates for core app data.
- Context keeps simple cross-component UI state lightweight.

## Deployment Steps (Azure App Service - Container)

### 1) Prerequisites

- Azure CLI installed
- Docker Desktop installed and running
- Logged in to Azure: `az login`

### 2) Create Azure resources (one-time)

```bash
az group create --name rg-blog-app --location centralindia
az acr create --resource-group rg-blog-app --name blogappacr3221178 --sku Basic
az appservice plan create --name asp-blog-app --resource-group rg-blog-app --is-linux --sku B1
az webapp create --resource-group rg-blog-app --plan asp-blog-app --name blog-app-3221178-39368 --deployment-container-image-name nginx
```

### 3) Build and push image

```bash
az acr login --name blogappacr3221178
docker build -t blogappacr3221178.azurecr.io/blog-app:latest .
docker push blogappacr3221178.azurecr.io/blog-app:latest
```

### 4) Configure Web App to use ACR image

```bash
az webapp config container set --name blog-app-3221178-39368 --resource-group rg-blog-app --container-image-name blogappacr3221178.azurecr.io/blog-app:latest --container-registry-url https://blogappacr3221178.azurecr.io
az webapp identity assign --name blog-app-3221178-39368 --resource-group rg-blog-app
```

Then assign `AcrPull`:

```bash
PRINCIPAL_ID=$(az webapp identity show --name blog-app-3221178-39368 --resource-group rg-blog-app --query principalId -o tsv)
ACR_ID=$(az acr show --name blogappacr3221178 --resource-group rg-blog-app --query id -o tsv)
az role assignment create --assignee $PRINCIPAL_ID --scope $ACR_ID --role AcrPull
```

### 5) Restart and verify

```bash
az webapp restart --name blog-app-3221178-39368 --resource-group rg-blog-app
az webapp show --name blog-app-3221178-39368 --resource-group rg-blog-app --query defaultHostName -o tsv
```

Live URL:

- https://blog-app-3221178-39368.azurewebsites.net

## Assumptions Made

- Node.js 20+ and npm are available.
- This is a frontend-only assignment; no backend database is used.
- Posts are intentionally stored in browser `localStorage`.
- `nginx.conf` handles SPA route fallback in production.
- Azure deployment uses a Docker container image served through Azure App Service.

